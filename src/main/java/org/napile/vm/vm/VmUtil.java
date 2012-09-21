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
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
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
	private static final Logger LOGGER = Logger.getLogger(VmUtil.class);

	public static void initBootStrap(Vm vm)
	{
		AssertUtil.assertNull(vm.getClass(NapileLangPackage.ANY));
		AssertUtil.assertNull(vm.getClass(NapileLangPackage.ARRAY));
		AssertUtil.assertNull(vm.getClass(NapileLangPackage.CHAR));
		AssertUtil.assertNull(vm.getClass(NapileLangPackage.STRING));
		AssertUtil.assertNull(vm.getClass(NapileLangPackage.INT));
		AssertUtil.assertNull(vm.getClass(NapileLangPackage.NULL));

		vm.moveFromBootClassLoader(); // change bootstrap class loader - to new instance
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
}
