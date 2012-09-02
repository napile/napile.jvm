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
import org.napile.compiler.lang.rt.NapileLangPackage;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.objectinfo.impl.BaseObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ByteObjectInfo;
import org.napile.vm.objects.objectinfo.impl.CharObjectInfo;
import org.napile.vm.objects.objectinfo.impl.DoubleObjectInfo;
import org.napile.vm.objects.objectinfo.impl.FloatObjectInfo;
import org.napile.vm.objects.objectinfo.impl.IntObjectInfo;
import org.napile.vm.objects.objectinfo.impl.LongObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ShortObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ValueObjectInfo;
import org.napile.vm.util.AssertUtil;

/**
 * @author VISTALL
 * @date 18:27/31.01.2012
 */
public class VmUtil
{
	private static final Logger LOGGER = Logger.getLogger(VmUtil.class);

	@Deprecated
	public static final BaseObjectInfo OBJECT_NULL = new ValueObjectInfo<Object>(null, null);

	public static void initBootStrap(Vm vm)
	{
		AssertUtil.assertNull(vm.getClass(NapileLangPackage.ANY));
		AssertUtil.assertNull(vm.getClass(NapileLangPackage.INT));

		vm.moveFromBootClassLoader(); // change bootstrap class loader - to new instance
	}

	public static BaseObjectInfo convertToVm(Vm vm, ClassInfo classInfo, Object object)
	{
		if(classInfo.getName().equals(NapileLangPackage.BYTE))
			return new ByteObjectInfo(classInfo, ((Number)object).byteValue());
		else if(classInfo.getName().equals(NapileLangPackage.SHORT))
			return new ShortObjectInfo(classInfo, ((Number)object).shortValue());
		else if(classInfo.getName().equals(NapileLangPackage.INT))
			return new IntObjectInfo(classInfo, ((Number)object).intValue());
		else if(classInfo.getName().equals(NapileLangPackage.LONG))
			return new LongObjectInfo(classInfo, ((Number)object).longValue());
		else if(classInfo.getName().equals(NapileLangPackage.FLOAT))
			return new FloatObjectInfo(classInfo, ((Number)object).floatValue());
		else if(classInfo.getName().equals(NapileLangPackage.DOUBLE))
			return new DoubleObjectInfo(classInfo, ((Number)object).doubleValue());
		else if(classInfo.getName().equals(NapileLangPackage.CHAR))
		{
			int val = (Integer)object;
			return new CharObjectInfo(classInfo, (char)val);
		}
		/**else if(classInfo.getName().equals(NapileLangPackage.STRING))
		{
			ClassInfo primitiveCharClassInfo = vm.getClass(NapileLangPackage.CHAR);
			ClassInfo primitiveCharClassArrayInfo = vm.getClass(NapileLangPackage.);

			char[] data = ((String)object).toCharArray();
			CharObjectInfo[] cData = new CharObjectInfo[data.length];
			for(int i = 0; i < data.length; i++)
				cData[i] = new CharObjectInfo(primitiveCharClassInfo, data[i]);

			ArrayObjectInfo arrayObjectInfo = new ArrayObjectInfo(primitiveCharClassArrayInfo, cData);

			return AssertUtil.assertNull(vm.newObject(classInfo, new String[]{Vm.PRIMITIVE_CHAR_ARRAY}, arrayObjectInfo));
		} */
		else
		{
			System.out.println(classInfo.getName() + " is not convertable. Value: " + object);
		}
			//throw new IllegalArgumentException(classInfo.getName() + " is not convertable. Value: " + object);

		return null;
	}

	public static List<VariableInfo> collectAllFields(@NotNull ClassInfo info)
	{
		List<VariableInfo> list = new ArrayList<VariableInfo>();
		for(ClassInfo classInfo : collectAllClasses(info))
			list.addAll(classInfo.getVariables());

		return list;
	}

	public static List<MethodInfo> collectAllMethods(ClassInfo info)
	{
		List<MethodInfo> list = new ArrayList<MethodInfo>();
		for(ClassInfo classInfo : collectAllClasses(info))
			list.addAll(classInfo.getMethods());

		return list;
	}

	@NotNull
	public static Set<ClassInfo> collectAllClasses(@NotNull ClassInfo classInfo)
	{
		Set<ClassInfo> result = new LinkedHashSet<ClassInfo>();
		result.add(classInfo);
		collectClasses(classInfo, result);
		return result;
	}

	private static void collectClasses(ClassInfo classInfo, Set<ClassInfo> set)
	{
		for(ClassInfo ci : classInfo.getExtends())
		{
			set.add(ci);

			collectClasses(ci, set);
		}
	}
}
