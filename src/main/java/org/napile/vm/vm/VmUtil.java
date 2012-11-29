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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.lib.NapileReflectPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeWrapper;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.util.AssertUtil;

/**
 * @author VISTALL
 * @date 18:27/31.01.2012
 */
public class VmUtil
{
	public static final TypeNode CHAR = new TypeNode(false, new ClassTypeNode(NapileLangPackage.CHAR));
	public static final TypeNode CLASS = new TypeNode(false, new ClassTypeNode(NapileReflectPackage.CLASS));
	public static final TypeNode TYPE = new TypeNode(false, new ClassTypeNode(NapileReflectPackage.TYPE));
	public static final TypeNode BOOL = new TypeNode(false, new ClassTypeNode(NapileLangPackage.BOOL));
	public static final TypeNode BYTE = new TypeNode(false, new ClassTypeNode(NapileLangPackage.BYTE));
	public static final TypeNode SHORT = new TypeNode(false, new ClassTypeNode(NapileLangPackage.SHORT));
	public static final TypeNode INT = new TypeNode(false, new ClassTypeNode(NapileLangPackage.INT));
	public static final TypeNode LONG = new TypeNode(false, new ClassTypeNode(NapileLangPackage.LONG));
	public static final TypeNode FLOAT = new TypeNode(false, new ClassTypeNode(NapileLangPackage.FLOAT));
	public static final TypeNode DOUBLE = new TypeNode(false, new ClassTypeNode(NapileLangPackage.DOUBLE));
	public static final TypeNode STRING = new TypeNode(false, new ClassTypeNode(NapileLangPackage.STRING));
	public static final TypeNode ARRAY__STRING__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(STRING);
	public static final TypeNode ARRAY__CHAR__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(CHAR);

	private static final Logger LOGGER = Logger.getLogger(VmUtil.class);

	public static void initBootStrap(Vm vm)
	{
		vm.safeGetClass(NapileLangPackage.ANY);
		vm.safeGetClass(NapileLangPackage.ARRAY);
		vm.safeGetClass(NapileLangPackage.CHAR);
		vm.safeGetClass(NapileLangPackage.BOOL);
		vm.safeGetClass(NapileLangPackage.STRING);
		vm.safeGetClass(NapileLangPackage.INT);
		vm.safeGetClass(NapileLangPackage.NULL);

		vm.moveFromBootClassLoader(); // change bootstrap class loader - to new instance

		NativeWrapper.initAll(vm);
	}

	public static BaseObjectInfo staticValue(@NotNull Vm vm, @NotNull FqName fqName, @NotNull String varName)
	{
		ClassInfo classInfo = vm.safeGetClass(fqName);
		vm.initStaticIfNeed(classInfo);

		VariableInfo variableInfo = null;
		for(VariableInfo v : classInfo.getVariables())
			if(v.getShortName().equals(varName))
				variableInfo = v;

		AssertUtil.assertNull(variableInfo);

		return variableInfo.getStaticValue();

	}

	public static BaseObjectInfo convertToVm(@NotNull Vm vm, @NotNull InterpreterContext context, @Nullable Object value)
	{
		if(value == null)
			return staticValue(vm, NapileLangPackage.NULL, "INSTANCE");
		else if(value instanceof String)
		{
			char[] chars = ((String) value).toCharArray();
			BaseObjectInfo arrayObject = vm.newObject(context, ARRAY__CHAR__, varargTypes(INT), new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, chars.length)});
			final BaseObjectInfo[] arrayAttach = arrayObject.value();
			for(int i = 0; i < chars.length; i++)
				arrayAttach[i] = convertToVm(vm, context, chars[i]);

			return vm.newObject(context, STRING, varargTypes(ARRAY__CHAR__), new BaseObjectInfo[] {arrayObject});
		}
		else if(value instanceof Character)
			return vm.newObject(CHAR).value(value);
		else if(value instanceof Byte)
			return vm.newObject(BYTE).value(value);
		else if(value instanceof Short)
			return vm.newObject(SHORT).value(value);
		else if(value instanceof Integer)
			return vm.newObject(INT).value(value);
		else if(value instanceof Long)
			return vm.newObject(LONG).value(value);
		else if(value instanceof Float)
			return vm.newObject(FLOAT).value(value);
		else if(value instanceof Double)
			return vm.newObject(DOUBLE).value(value);
		else if(value instanceof Boolean)
			return staticValue(vm, NapileLangPackage.BOOL, value == Boolean.TRUE ? "TRUE" : "FALSE");
		else
			throw new UnsupportedOperationException(value.getClass().getName());
	}

	@NotNull
	public static List<VariableInfo> collectAllFields(@NotNull Vm vm, @NotNull ClassInfo info)
	{
		List<VariableInfo> list = new ArrayList<VariableInfo>();
		for(ClassInfo classInfo : collectAllClasses(vm, info))
			list.addAll(classInfo.getVariables());

		return list;
	}

	@NotNull
	public static List<MethodInfo> collectAllMethods(@NotNull Vm vm, @NotNull ClassInfo info)
	{
		List<MethodInfo> list = new ArrayList<MethodInfo>();
		for(ClassInfo classInfo : collectAllClasses(vm, info))
			list.addAll(classInfo.getMethods());

		return list;
	}

	@NotNull
	public static Set<ClassInfo> collectAllClasses(@NotNull Vm vm, @NotNull ClassInfo classInfo)
	{
		Set<ClassInfo> result = new LinkedHashSet<ClassInfo>();
		result.add(classInfo);
		collectClasses(vm, classInfo, result);
		return result;
	}

	private static void collectClasses(@NotNull Vm vm, ClassInfo classInfo, Set<ClassInfo> set)
	{
		for(TypeNode ci : classInfo.getExtends())
		{
			ClassTypeNode classTypeNode = (ClassTypeNode) ci.typeConstructorNode;

			ClassInfo superType = vm.getClass(classTypeNode.className);

			AssertUtil.assertNull(superType);

			set.add(superType);

			collectClasses(vm, superType, set);
		}
	}

	public static TypeNode[] varargTypes(@NotNull TypeNode... a)
	{
		return a;
	}
}
