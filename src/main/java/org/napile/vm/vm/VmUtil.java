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
import org.napile.asm.AsmConstants;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.lib.NapileReflectPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.asm.tree.members.types.constructors.TypeConstructorNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.util.AssertUtil;

/**
 * @author VISTALL
 * @since 18:27/31.01.2012
 */
public class VmUtil
{
	public static final FqName VM_MAIN_CALLER = new FqName("org.napile.vm.MainCaller");
	public static final FqName INVALID = new FqName("napile.lang.Invalid");

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
	public static final TypeNode STRING = AsmConstants.STRING_TYPE;
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
		vm.safeGetClass(INVALID);
		vm.safeGetClass(VM_MAIN_CALLER);

		vm.moveFromBootClassLoader(); // change bootstrap class loader - to new instance
	}

	public static BaseObjectInfo staticValue(@NotNull Vm vm, @NotNull FqName fqName, @NotNull String varName)
	{
		ClassInfo classInfo = vm.safeGetClass(fqName);
		vm.initStaticIfNeed(classInfo);

		VariableInfo variableInfo = null;
		for(VariableInfo v : classInfo.getVariables())
			if(v.getName().equals(varName))
				variableInfo = v;

		AssertUtil.assertNull(variableInfo);

		return classInfo.getObjectForStatic().getVarValue(variableInfo);

	}

	@NotNull
	public static BaseObjectInfo convertToVm(@NotNull Vm vm, @NotNull InterpreterContext context, @Nullable Object value)
	{
		if(value == null)
			return staticValue(vm, NapileLangPackage.NULL, "INSTANCE");
		else if(value instanceof String)
		{
			char[] chars = ((String) value).toCharArray();

			ClassInfo stringClassInfo = vm.safeGetClass(NapileLangPackage.STRING);
			BaseObjectInfo stringObject = new BaseObjectInfo(vm, stringClassInfo, AsmConstants.STRING_TYPE, false);
			stringObject.initializeVariables(null, context, vm);
			stringObject.setVarValue(vm.getField(stringClassInfo, "count", false), VmUtil.convertToVm(vm, context, chars.length));
			stringObject.setVarValue(vm.getField(stringClassInfo, "offset", false), VmUtil.convertToVm(vm, context, 0));

			BaseObjectInfo arrayObject = createArray(vm, context, ARRAY__CHAR__, chars.length);
			stringObject.setVarValue(vm.getField(stringClassInfo, "array", false), arrayObject);

			final BaseObjectInfo[] arrayAttach = arrayObject.value();
			for(int i = 0; i < chars.length; i++)
				arrayAttach[i] = convertToVm(vm, context, chars[i]);

			return stringObject;
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

	public static BaseObjectInfo createArray(Vm vm, InterpreterContext context, TypeNode type, int size)
	{
		ClassInfo classInfo = vm.safeGetClass(NapileLangPackage.ARRAY);

		BaseObjectInfo baseObjectInfo = new BaseObjectInfo(vm, classInfo, type, false);
		baseObjectInfo.initializeVariables(null, context, vm);
		baseObjectInfo.setVarValue(vm.getField(classInfo, "length", false), vm.newObject(INT).value(size));
		BaseObjectInfo[] value = new BaseObjectInfo[size];
		for(int i = 0; i < value.length; i++)
			value[i] = staticValue(vm, NapileLangPackage.NULL, "INSTANCE");

		baseObjectInfo.value(value);
		return baseObjectInfo;
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public static <T> T convertToJava(@NotNull Vm vm, @NotNull BaseObjectInfo val)
	{
		TypeNode typeNode = val.getTypeNode();
		TypeConstructorNode constructorNode = typeNode.typeConstructorNode;
		if(!(constructorNode instanceof ClassTypeNode))
			throw new UnsupportedOperationException(typeNode.toString());
		FqName fqName = ((ClassTypeNode) constructorNode).className;
		if(fqName.equals(NapileLangPackage.STRING))
		{
			BaseObjectInfo baseObjectInfo = val.getVarValue(vm.getField(vm.safeGetClass(NapileLangPackage.STRING), "array", false));
			baseObjectInfo.initializeVariables(null, new InterpreterContext(), vm);

			BaseObjectInfo[] attach = baseObjectInfo.value();
			StringBuilder b = new StringBuilder();
			for(BaseObjectInfo i : attach)
				b.append(i.value());
			return (T) b.toString();
		}
		else if(fqName.equals(NapileLangPackage.INT) || fqName.equals(NapileLangPackage.LONG) || fqName.equals(NapileLangPackage.SHORT) || fqName.equals(NapileLangPackage.BYTE))
			return val.value();
		else if(fqName.equals(NapileLangPackage.ARRAY))
		{
			Integer length = convertToJava(vm, val.getVarValue(vm.getField(vm.safeGetClass(NapileLangPackage.ARRAY), "length", false)));

			BaseObjectInfo[] values = val.value();
			TypeNode argument = typeNode.arguments.get(0);
			FqName argFq = ((ClassTypeNode)argument.typeConstructorNode).className;
			if(argFq.equals(NapileLangPackage.BYTE))
			{
				byte[] data = new byte[length];
				for(int i = 0; i < data.length; i++)
					data[i] = (Byte) convertToJava(vm, values[i]);
				return (T) data;
			}
		}

		throw new UnsupportedOperationException(fqName.toString());
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
	public static List<MethodInfo> collectAllMacros(@NotNull Vm vm, @NotNull ClassInfo info)
	{
		List<MethodInfo> list = new ArrayList<MethodInfo>();
		for(ClassInfo classInfo : collectAllClasses(vm, info))
			list.addAll(classInfo.getMacros());

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
