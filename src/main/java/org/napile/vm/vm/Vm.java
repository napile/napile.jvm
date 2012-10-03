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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.asm.io.text.in.type.TypeNodeUtil;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.resolve.name.Name;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.asm.tree.members.types.constructors.TypeConstructorNode;
import org.napile.vm.classloader.JClassLoader;
import org.napile.vm.classloader.impl.SimpleClassLoaderImpl;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
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
 * @date 17:26/31.01.2012
 */
public class Vm
{
	private static final Logger LOGGER = Logger.getLogger(Vm.class);

	private VmContext _vmContext;

	private JClassLoader _bootClassLoader = new SimpleClassLoaderImpl(null);
	private JClassLoader _currentClassLoader = _bootClassLoader;

	private Map<ClassInfo, BaseObjectInfo> _initClasses = new HashMap<ClassInfo, BaseObjectInfo>();

	public Vm(VmContext vmContext)
	{
		_vmContext = vmContext;
	}

	public ClassInfo getClass(FqName name)
	{
		return ClasspathUtil.getClassInfoOrParse(this, name);
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

	public void invoke(MethodInfo methodInfo, BaseObjectInfo object, InterpreterContext context, BaseObjectInfo... argument)
	{
		initStaticIfNeed(methodInfo.getParent());

		if(!methodInfo.getName().equals(MethodInfo.CONSTRUCTOR_NAME))
			AssertUtil.assertTrue(methodInfo.hasModifier(Modifier.STATIC) && object != null || !methodInfo.hasModifier(Modifier.STATIC) && object == null);

		InvokeType invokeType = methodInfo.getInvokeType();

		AssertUtil.assertNull(invokeType);

		invokeType.call(this, context == null ? new InterpreterContext(new StackEntry(object, methodInfo, argument)) : context);
	}

	@NotNull
	public BaseObjectInfo newObject(@NotNull TypeNode typeNode)
	{
		TypeConstructorNode typeConstructorNode = typeNode.typeConstructorNode;
		if(typeConstructorNode instanceof ClassTypeNode)
		{
			ClassInfo classInfo = getClass(((ClassTypeNode) typeConstructorNode).className);

			initStaticIfNeed(classInfo);

			return new BaseObjectInfo(this, classInfo, typeNode);
		}
		else
			throw new UnsupportedOperationException("This type constructor is cant be created by 'newObject' " + typeConstructorNode);
	}

	@NotNull
	public BaseObjectInfo newObject(@NotNull TypeNode typeNode, TypeNode[] constructorTypes, BaseObjectInfo[] arguments)
	{
		BaseObjectInfo newObject = newObject(typeNode);

		MethodInfo methodInfo = AssertUtil.assertNull(getMethod(newObject.getClassInfo(), MethodInfo.CONSTRUCTOR_NAME.getName(), false, constructorTypes));

		invoke(methodInfo, newObject, null, arguments);

		return newObject;
	}

	public VmContext getVmContext()
	{
		return _vmContext;
	}

	public JClassLoader getBootClassLoader()
	{
		return _bootClassLoader;
	}

	public JClassLoader getCurrentClassLoader()
	{
		return _currentClassLoader;
	}

	public JClassLoader moveFromBootClassLoader()
	{
		_currentClassLoader = new SimpleClassLoaderImpl(_currentClassLoader);
		return _currentClassLoader;
	}

	public VariableInfo getField0(final ClassInfo info, String name, boolean deep)
	{
		FqName fieldName = info.getName().child(Name.identifier(name));

		List<VariableInfo> variableInfos = deep ? VmUtil.collectAllFields(this, info) : info.getVariables();
		for(VariableInfo variableInfo : variableInfos)
			if(variableInfo.getName().equals(fieldName))
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

	public MethodInfo getMethod0(ClassInfo info, String name, boolean deep, TypeNode[] params)
	{
		List<MethodInfo> methodInfos =  deep ? VmUtil.collectAllMethods(this, info) : info.getMethods();

		for(MethodInfo methodInfo : methodInfos)
		{
			if(!methodInfo.getName().shortName().getName().equals(name))
				continue;

			if(!Comparing.equal(params, methodInfo.getParameters()))
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
				MethodInfo methodInfo = getStaticMethod(ownerClassInfo, MethodInfo.STATIC_CONSTRUCTOR_NAME.getName(), false, ArrayUtil.EMPTY_STRING_ARRAY);

				if(methodInfo != null)
				{
					LOGGER.debug("Static constructor call: " + ownerClassInfo);
					StackEntry stackEntry = new StackEntry(null, methodInfo, BaseObjectInfo.EMPTY_ARRAY);
					InterpreterContext contextMain = /*context == null ?*/ new InterpreterContext(stackEntry);// : context;
					//if(context == null)
					//	contextMain.getStack().add(stackEntry);

					invoke(methodInfo, null, contextMain, BaseObjectInfo.EMPTY_ARRAY);

					//contextMain.getStack().pollLast();
				}
			}
		}
	}
}
