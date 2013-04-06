/*
 * Copyright 2010-2012 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.vm.vm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.napile.asm.AsmConstants;
import org.napile.asm.Modifier;
import org.napile.asm.io.text.in.type.TypeNodeUtil;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.lib.NapileReflectPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.resolve.name.Name;
import org.napile.asm.tree.members.MethodNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.asm.tree.members.types.constructors.MethodTypeNode;
import org.napile.asm.tree.members.types.constructors.MultiTypeNode;
import org.napile.asm.tree.members.types.constructors.TypeConstructorNode;
import org.napile.asm.tree.members.types.constructors.TypeParameterValueTypeNode;
import org.napile.asm.util.Comparing2;
import org.napile.vm.classloader.JClassLoader;
import org.napile.vm.classloader.impl.SimpleClassLoaderImpl;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.CallPosition;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3.VmNewObjectInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.util.ClasspathUtil;
import com.intellij.openapi.util.Comparing;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 17:26/31.01.2012
 */
public class Vm
{
	private static final TypeNode NAPILE_LANG_ARRAY__TYPE____ANY____ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(new TypeNode(false, new ClassTypeNode(NapileReflectPackage.TYPE)).visitArgument(AsmConstants.ANY_TYPE));
	private static final Logger LOGGER = Logger.getLogger(Vm.class);

	private final VmContext vmContext;

	private final JClassLoader bootClassLoader = new SimpleClassLoaderImpl(null);
	private JClassLoader currentClassLoader = bootClassLoader;


	public Vm(VmContext vmContext)
	{
		this.vmContext = vmContext;
	}

	public ClassInfo getClass(FqName name)
	{
		return ClasspathUtil.getClassInfoOrParse(this, name);
	}

	@NotNull
	public ClassInfo safeGetClass(FqName name)
	{
		ClassInfo classInfo = getClass(name);
		if(classInfo == null)
		{
			LOGGER.error("Class not found " + name);
			System.exit(-1);
		}
		return classInfo;
	}

	public VariableInfo getField(ClassInfo info, String name, boolean deep)
	{
		VariableInfo variableInfo = getField0(info, name, deep);
		return variableInfo != null && !variableInfo.hasModifier(Modifier.STATIC) ? variableInfo : null;
	}

	public VariableInfo getStaticField(ClassInfo info, String name, boolean deep)
	{
		VariableInfo variableInfo = getField0(info, name, deep);
		return variableInfo != null && variableInfo.hasModifier(Modifier.STATIC) ? variableInfo : null;
	}

	public VariableInfo getAnyField(ClassInfo info, String name, boolean deep)
	{
		return getField0(info, name, deep);
	}

	@Deprecated
	public MethodInfo getMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && !methodInfo.hasModifier(Modifier.STATIC) ? methodInfo : null;
	}

	public MethodInfo getMethod(ClassInfo info, String name, boolean deep, TypeNode... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && !methodInfo.hasModifier(Modifier.STATIC) ? methodInfo : null;
	}

	public MethodInfo getStaticMethod(ClassInfo info, String name, boolean deep, TypeNode... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && methodInfo.hasModifier(Modifier.STATIC) ? methodInfo : null;
	}

	public MethodInfo getStaticMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && methodInfo.hasModifier(Modifier.STATIC) ? methodInfo : null;
	}

	public MethodInfo getAnyMethod(ClassInfo info, String name, boolean deep, TypeNode... params)
	{
		return getMethod0(info, name, deep, params);
	}

	public MethodInfo getAnyMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		return getMethod0(info, name, deep, params);
	}

	public MethodInfo getMacro(ClassInfo info, String name, boolean deep, TypeNode... params)
	{
		MethodInfo methodInfo = getMacro0(info, name, deep, params);
		return methodInfo != null && !methodInfo.hasModifier(Modifier.STATIC) ? methodInfo : null;
	}

	public MethodInfo getStaticMacro(ClassInfo info, String name, boolean deep, TypeNode... params)
	{
		MethodInfo methodInfo = getMacro0(info, name, deep, params);
		return methodInfo != null && methodInfo.hasModifier(Modifier.STATIC) ? methodInfo : null;
	}

	public void invoke(@NotNull InterpreterContext context, @NotNull InvokeType invokeType)
	{
		StackEntry stackEntry = context.getLastStack();

		MethodInfo methodInfo = stackEntry.getMethodInfo();

		BaseObjectInfo objectInfo = stackEntry.getObjectInfo();

		if(methodInfo != null)
		{
			initStaticIfNeed(methodInfo.getParent());

			if(!methodInfo.getFqName().shortName().equals(MethodNode.CONSTRUCTOR_NAME))
				AssertUtil.assertTrue(methodInfo.hasModifier(Modifier.STATIC) && objectInfo != null || !methodInfo.hasModifier(Modifier.STATIC) && objectInfo == null);

			if(methodInfo.hasModifier(Modifier.ABSTRACT))
				AssertUtil.assertString("Trying to invoke 'abstract' method: " + methodInfo);
		}

		invokeType.call(this, context);
	}

	@NotNull
	public BaseObjectInfo newObject(@NotNull TypeNode typeNode)
	{
		TypeConstructorNode typeConstructorNode = typeNode.typeConstructorNode;
		if(typeConstructorNode instanceof ClassTypeNode)
		{
			ClassInfo classInfo = safeGetClass(((ClassTypeNode) typeConstructorNode).className);

			initStaticIfNeed(classInfo);

			return new BaseObjectInfo(this, classInfo, typeNode);
		}
		else
			throw new UnsupportedOperationException("This type constructor is cant be created by 'newObject' " + typeConstructorNode);
	}

	@NotNull
	public BaseObjectInfo newObject(@NotNull InterpreterContext context, VmNewObjectInstruction instruction, @NotNull TypeNode typeNode, TypeNode[] constructorTypes, BaseObjectInfo[] arguments)
	{
		BaseObjectInfo newObject = newObject(typeNode);

		MethodInfo methodInfo = AssertUtil.assertNull(getMethod(newObject.getClassInfo(), MethodNode.CONSTRUCTOR_NAME.getName(), false, constructorTypes), "Constructor not found for: " + typeNode + " constructorTypes: " + Arrays.toString(constructorTypes));

		StackEntry stackEntry = new StackEntry(newObject, methodInfo, arguments, typeNode.arguments);
		stackEntry.position = new CallPosition(instruction);

		context.getStack().add(stackEntry);

		invoke(context, methodInfo.getInvokeType());

		context.getStack().remove(stackEntry);

		return newObject;
	}

	@NotNull
	public BaseObjectInfo getOrCreateClassObject(@NotNull InterpreterContext context, @NotNull ClassInfo classInfo)
	{
		BaseObjectInfo objectInfo = classInfo.getClassObjectInfo();
		if(objectInfo != null)
			return objectInfo;

		TypeNode typeNode = new TypeNode(false, new ClassTypeNode(NapileReflectPackage.CLASS));
		typeNode.arguments.add(new TypeNode(false, new ClassTypeNode(classInfo.getFqName())));

		BaseObjectInfo classObjectInfo = VmReflectUtil.createReflectObject(typeNode, VmUtil.convertToVm(this, context, null), this, context, classInfo);
		classInfo.setClassObjectInfo(classObjectInfo);
		return classObjectInfo;
	}

	@NotNull
	public BaseObjectInfo createTypeObject(@NotNull InterpreterContext context, @NotNull TypeNode t)
	{
		TypeNode targetType = toType(context, t);

		TypeNode newObjectType = new TypeNode(false, new ClassTypeNode(NapileReflectPackage.TYPE));
		newObjectType.visitArgument(targetType);

		BaseObjectInfo array = VmUtil.createArray(this, NAPILE_LANG_ARRAY__TYPE____ANY____, targetType.arguments.size());
		BaseObjectInfo[] values = array.value();
		for(int i = 0; i < values.length; i++)
			values[i] = createTypeObject(context, targetType.arguments.get(i));

		return newObject(context, null, newObjectType,
				new TypeNode[]
				{
					new TypeNode(false, new ClassTypeNode(NapileReflectPackage.CLASS)).visitArgument(new TypeNode(false, new TypeParameterValueTypeNode(Name.identifier("E")))),
						NAPILE_LANG_ARRAY__TYPE____ANY____,
						AsmConstants.BOOL_TYPE,
					VmReflectUtil.NAPILE_LANG_ARRAY__ANY__
				},
				new BaseObjectInfo[]
				{
					getOrCreateClassObject(context, safeGetClass(toClassType(context, t).className)),
					array,
					VmUtil.convertToVm(this, context, targetType.nullable),
					VmReflectUtil.createArray$Any$Annotations(this, context, targetType.annotations)
				});
	}

	private boolean canAcceptTypes(@NotNull TypeNode[] original, @NotNull TypeNode[] target)
	{
		if(original.length != target.length)
		{
			return false;
		}

		for(int i = 0; i < original.length; i++)
		{
			if(!canAcceptType(original[i], target[i]))
			{
				return false;
			}
		}
		return true;
	}

	private boolean canAcceptType(@NotNull TypeNode original, @NotNull TypeNode target)
	{
		if(!target.typeConstructorNode.equals(original.typeConstructorNode))
			return false;

		if(!Comparing2.equal(target.arguments, original.arguments))
			return false;

		return target.nullable == original.nullable;
	}

	public boolean isEqualOrSubType(@NotNull InterpreterContext context, @NotNull TypeNode target, @NotNull TypeNode toCheck)
	{
		// check first type constructor
		if(!isEqualOrSubType0(context, target, toCheck))
			return false;

		if(!target.arguments.isEmpty())
		{
			if(target.arguments.size() != toCheck.arguments.size())
				return false;

			for(int i = 0; i < target.arguments.size(); i++)
			{
				TypeNode toCheckArg = toCheck.arguments.get(i);
				TypeNode targetArg = target.arguments.get(i);
				if(!isEqualOrSubType(context, targetArg, toCheckArg))
					return false;
			}
		}

		return true;
	}

	private boolean isEqualOrSubType0(@NotNull InterpreterContext context, @NotNull final TypeNode target, @NotNull final TypeNode toCheck)
	{
		ClassTypeNode targetClassType = toClassType(context, target);
		ClassTypeNode toCheckClassType = toClassType(context, toCheck);

		if(toCheckClassType.equals(targetClassType))
			return true;

		if(!target.nullable && toCheck.nullable)
			return false;

		ClassInfo toCheckClassInfo = safeGetClass(toCheckClassType.className);

		for(TypeNode extendType : toCheckClassInfo.getExtends())
			if(isEqualOrSubType(context, target, extendType))
				return true;

		return false;
	}

	@NotNull
	private ClassTypeNode toClassType(@NotNull InterpreterContext context, @NotNull TypeNode typeNode)
	{
		if(typeNode.typeConstructorNode instanceof ClassTypeNode)
			return (ClassTypeNode) typeNode.typeConstructorNode;
		else if(typeNode.typeConstructorNode instanceof TypeParameterValueTypeNode)
		{
			TypeNode typeParameterType = context.searchTypeParameterValue(((TypeParameterValueTypeNode) typeNode.typeConstructorNode).name);
			return toClassType(context, typeParameterType);
		}
		else if(typeNode.typeConstructorNode instanceof MethodTypeNode)
		{
			return new ClassTypeNode(NapileLangPackage.ANONYM_CONTEXT);
		}
		else if(typeNode.typeConstructorNode instanceof MultiTypeNode)
		{
			return new ClassTypeNode(NapileLangPackage.MULTI);
		}
		else
			throw new UnsupportedOperationException(typeNode.typeConstructorNode.getClass().getName() + " is not supported");
	}

	@NotNull
	private TypeNode toType(@NotNull InterpreterContext context, @NotNull TypeNode typeNode)
	{
		if(typeNode.typeConstructorNode instanceof ClassTypeNode)
			return typeNode;
		else if(typeNode.typeConstructorNode instanceof TypeParameterValueTypeNode)
		{
			TypeNode typeParameterType = context.searchTypeParameterValue(((TypeParameterValueTypeNode) typeNode.typeConstructorNode).name);
			return toType(context, typeParameterType);
		}
		else if(typeNode.typeConstructorNode instanceof MethodTypeNode)
		{
			return new TypeNode(false, new ClassTypeNode(NapileLangPackage.ANONYM_CONTEXT));
		}
		else if(typeNode.typeConstructorNode instanceof MultiTypeNode)
		{
			return new TypeNode(false, new ClassTypeNode(NapileLangPackage.MULTI));
		}
		else
			throw new UnsupportedOperationException(typeNode.typeConstructorNode.getClass().getName() + " is not supported");
	}

	public VmContext getVmContext()
	{
		return vmContext;
	}

	public JClassLoader getBootClassLoader()
	{
		return bootClassLoader;
	}

	public JClassLoader getCurrentClassLoader()
	{
		return currentClassLoader;
	}

	public JClassLoader moveFromBootClassLoader()
	{
		currentClassLoader = new SimpleClassLoaderImpl(currentClassLoader);
		return currentClassLoader;
	}

	public VariableInfo getField0(final ClassInfo info, String name, boolean deep)
	{
		FqName fieldName = info.getFqName().child(Name.identifier(name));

		List<VariableInfo> variableInfos = deep ? VmUtil.collectAllFields(this, info) : info.getVariables();
		for(VariableInfo variableInfo : variableInfos)
			if(variableInfo.getFqName().equals(fieldName))
				return variableInfo;
		return null;
	}

	public MethodInfo getMethod0(ClassInfo info, String name, boolean deep, String... params)
	{
		TypeNode[] typeParams = new TypeNode[params.length];
		for(int i = 0; i < typeParams.length; i++)
			typeParams[i] = TypeNodeUtil.fromString(params[i]);

		return getMethod0(info, name, deep, typeParams);
	}

	public MethodInfo getMacro0(ClassInfo info, String name, boolean deep, TypeNode[] params)
	{
		List<MethodInfo> methodInfos =  deep ? VmUtil.collectAllMacros(this, info) : info.getMacros();

		for(MethodInfo methodInfo : methodInfos)
		{
			if(!methodInfo.getFqName().shortName().getName().equals(name))
				continue;

			if(!Comparing.equal(params, methodInfo.getParameters()))
				continue;

			return methodInfo;
		}
		return null;
	}

	public MethodInfo getMethod0(ClassInfo info, String name, boolean deep, TypeNode[] params)
	{
		List<MethodInfo> methodInfos =  deep ? VmUtil.collectAllMethods(this, info) : info.getMethods();

		for(MethodInfo methodInfo : methodInfos)
		{
			if(!methodInfo.getName().equals(name))
				continue;

			if(!canAcceptTypes(params, methodInfo.getParameters()))
				continue;

			return methodInfo;
		}
		return null;
	}

	public synchronized void initStaticIfNeed(@NotNull ClassInfo parent)
	{
		if(!parent.isStaticConstructorCalled())
		{
			for(ClassInfo ownerClassInfo : VmUtil.collectAllClasses(this, parent))
			{
				if(ownerClassInfo.isStaticConstructorCalled())
					continue;

				ownerClassInfo.setStaticConstructorCalled(true);
				MethodInfo methodInfo = getStaticMethod(ownerClassInfo, MethodNode.STATIC_CONSTRUCTOR_NAME.getName(), false, ArrayUtil.EMPTY_STRING_ARRAY);

				if(methodInfo != null)
				{
					LOGGER.debug("Static constructor call: " + ownerClassInfo);

					invoke(new InterpreterContext(new StackEntry(null, methodInfo, BaseObjectInfo.EMPTY_ARRAY, Collections.<TypeNode>emptyList())), methodInfo.getInvokeType());
				}
			}
		}
	}
}
