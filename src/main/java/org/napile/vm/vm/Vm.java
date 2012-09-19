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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.asm.lib.NapileReflectPackage;
import org.napile.asm.parsing.type.TypeNodeUtil;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.resolve.name.Name;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.util.Comparing2;
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
import org.napile.vm.util.CollectionUtil;

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

	public BaseObjectInfo getClassObjectInfo(ClassInfo classInfo)
	{
		BaseObjectInfo classObjectInfo = _initClasses.get(classInfo);
		if(classObjectInfo == null)
		{
			classObjectInfo = newObject(getClass(NapileReflectPackage.CLASS), CollectionUtil.EMPTY_STRING_ARRAY, BaseObjectInfo.EMPTY_ARRAY);

			_initClasses.put(classInfo, classObjectInfo);
		}

		return classObjectInfo;
	}

	public VariableInfo getField(ClassInfo info, String name, boolean deep)
	{
		VariableInfo variableInfo = getField0(info, name, deep);
		return variableInfo != null && !variableInfo.getFlags().contains(Modifier.STATIC) ? variableInfo : null;
	}

	public VariableInfo getStaticField(ClassInfo info, String name, boolean deep)
	{
		VariableInfo variableInfo = getField0(info, name, deep);
		return variableInfo != null && variableInfo.getFlags().contains(Modifier.STATIC) ? variableInfo : null;
	}

	public VariableInfo getAnyField(ClassInfo info, String name, boolean deep)
	{
		return getField0(info, name, deep);
	}

	public MethodInfo getMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && !methodInfo.getFlags().contains(Modifier.STATIC) ? methodInfo : null;
	}

	public MethodInfo getMethod(ClassInfo info, String name, boolean deep, List<TypeNode> params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && !methodInfo.getFlags().contains(Modifier.STATIC) ? methodInfo : null;
	}

	public MethodInfo getStaticMethod(ClassInfo info, String name, boolean deep, List<TypeNode> params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && methodInfo.getFlags().contains(Modifier.STATIC) ? methodInfo : null;
	}

	public MethodInfo getStaticMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && methodInfo.getFlags().contains(Modifier.STATIC) ? methodInfo : null;
	}

	public MethodInfo getAnyMethod(ClassInfo info, String name, boolean deep, List<TypeNode> params)
	{
		return getMethod0(info, name, deep, params);
	}

	public MethodInfo getAnyMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		return getMethod0(info, name, deep, params);
	}

	public void invoke(MethodInfo methodInfo, BaseObjectInfo object, InterpreterContext context, BaseObjectInfo... argument)
	{
		initStatic(methodInfo.getParent(), context);

		if(!methodInfo.getName().equals(MethodInfo.CONSTRUCTOR_NAME))
			AssertUtil.assertTrue(methodInfo.getFlags().contains(Modifier.STATIC) && object != null || !methodInfo.getFlags().contains(Modifier.STATIC) && object == null);

		InvokeType invokeType = methodInfo.getInvokeType();

		AssertUtil.assertNull(invokeType);

		invokeType.call(this, context == null ? new InterpreterContext(new StackEntry(object, methodInfo, argument)) : context);
	}

	public BaseObjectInfo newObject(ClassInfo classInfo, String[] constructorTypes, BaseObjectInfo... arguments)
	{
		initStatic(classInfo, null);

		MethodInfo methodInfo = AssertUtil.assertNull(getMethod(classInfo, MethodInfo.CONSTRUCTOR_NAME.getName(), false, constructorTypes));

		BaseObjectInfo classObjectInfo = new BaseObjectInfo(this, classInfo);

		invoke(methodInfo, classObjectInfo, null, arguments);

		return classObjectInfo;
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
		List<TypeNode> typeParams = new ArrayList<TypeNode>(params.length);
		for(String param : params)
			typeParams.add(TypeNodeUtil.fromString(param));

		return getMethod0(info, name, deep, typeParams);
	}

	public MethodInfo getMethod0(ClassInfo info, String name, boolean deep, List<TypeNode> params)
	{
		List<MethodInfo> methodInfos =  deep ? VmUtil.collectAllMethods(this, info) : info.getMethods();
		FqName methodName = info.getName().child(Name.identifier(name));
		for(MethodInfo methodInfo : methodInfos)
		{
			if(!methodInfo.getName().equals(methodName))
				continue;

			if(!Comparing2.equal(params, methodInfo.getParameters()))
				continue;

			return methodInfo;
		}
		return null;
	}

	private synchronized void initStatic(@NotNull ClassInfo parent, InterpreterContext context)
	{
		if(!parent.isStaticConstructorCalled())
		{
			for(ClassInfo ownerClassInfo : VmUtil.collectAllClasses(this, parent))
			{
				if(ownerClassInfo.isStaticConstructorCalled())
					continue;

				ownerClassInfo.setStaticConstructorCalled(true);
				MethodInfo methodInfo = getStaticMethod(ownerClassInfo, MethodInfo.STATIC_CONSTRUCTOR_NAME.getName(), false);

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
