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

package org.napile.vm.invoke.impl.nativeimpl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.napile.asm.parsing.type.TypeNodeUtil;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.resolve.name.Name;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.util.Comparing2;
import org.napile.vm.invoke.impl.nativeimpl.classes.codegenTest_MyTest;
import org.napile.vm.invoke.impl.nativeimpl.classes.napile_lang_Any;
import org.napile.vm.invoke.impl.nativeimpl.classes.napile_lang_Int;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 23:59/16.02.2012
 */
public class NativeWrapper
{
	private static Map<ClassInfo, List<NativeMethodRef>> WRAPPERS = new HashMap<ClassInfo, List<NativeMethodRef>>();

	public static void initAll(Vm vm)
	{
		register(vm, napile_lang_Int.class);
		register(vm, napile_lang_Any.class);
		register(vm, codegenTest_MyTest.class);
	}

	private static void register(Vm vm, Class<?> clazz)
	{
		for(Method method : clazz.getDeclaredMethods())
		{
			if(!Modifier.isStatic(method.getModifiers()))
				continue;

			NativeImplement nativeImplement = method.getAnnotation(NativeImplement.class);
			if(nativeImplement == null)
				continue;

			AssertUtil.assertTrue(method.getParameterTypes().length != 3 || method.getParameterTypes()[0] != Vm.class || method.getParameterTypes()[1] != BaseObjectInfo.class || method.getParameterTypes()[2] != BaseObjectInfo[].class);

			final FqName className = new FqName(nativeImplement.className());

			final ClassInfo classInfo = vm.getClass(className);

			List<NativeMethodRef> list = WRAPPERS.get(classInfo);
			if(list == null)
				WRAPPERS.put(classInfo, list = new ArrayList<NativeMethodRef>());

			List<TypeNode> params = new ArrayList<TypeNode>(nativeImplement.parameters().length);
			for(String param : nativeImplement.parameters())
				params.add(TypeNodeUtil.fromString(param));

			NativeMethodRef methodInfo = new NativeMethodRef(className.child(Name.identifier(nativeImplement.methodName())), params, method);

			list.add(methodInfo);
		}
	}

	public static NativeMethodRef getMethod(ClassInfo classInfo, FqName name, List<TypeNode> params)
	{
		List<NativeMethodRef> nativeMethodRefs = WRAPPERS.get(classInfo);
		if(nativeMethodRefs == null)
			return null;

		for(NativeMethodRef methodInfo : nativeMethodRefs)
		{
			if(!methodInfo.getName().equals(name))
				continue;

			if(!Comparing2.equal(methodInfo.getParameters(), params))
				continue;

			return methodInfo;
		}

		return null;
	}
}
