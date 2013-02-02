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

import org.napile.asm.io.text.in.type.TypeNodeUtil;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.resolve.name.Name;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import com.intellij.openapi.util.Comparing;

/**
 * @author VISTALL
 * @date 23:59/16.02.2012
 */
public class NativeWrapper
{
	private final static Map<ClassInfo, List<NativeMethodRef>> nativeWrappers = new HashMap<ClassInfo, List<NativeMethodRef>>();

	private static List<NativeMethodRef> register(Class<?> clazz, ClassInfo classInfo)
	{
		List<NativeMethodRef> list = new ArrayList<NativeMethodRef>();

		nativeWrappers.put(classInfo, list);

		for(Method method : clazz.getDeclaredMethods())
		{
			if(!Modifier.isStatic(method.getModifiers()))
				continue;

			NativeImplement nativeImplement = method.getAnnotation(NativeImplement.class);
			if(nativeImplement == null)
				continue;

			AssertUtil.assertTrue(method.getParameterTypes().length != 2 || method.getParameterTypes()[0] != Vm.class || method.getParameterTypes()[1] != InterpreterContext.class);

 			int i = 0;
			TypeNode[] params = new TypeNode[nativeImplement.parameters().length];
			for(String param : nativeImplement.parameters())
				params[i++] = TypeNodeUtil.fromString(param);

			NativeMethodRef methodInfo = new NativeMethodRef(classInfo.getFqName().child(Name.identifier(nativeImplement.methodName())), params, method);

			list.add(methodInfo);
		}
		return list;
	}

	public static NativeMethodRef getMethod(Vm vm, ClassInfo classInfo, FqName name, TypeNode[] params)
	{
		FqName parentFq = name.parent();

		List<NativeMethodRef> nativeMethodRefs = nativeWrappers.get(classInfo);
		if(nativeMethodRefs == null)
		{
			try
			{
				Class<?> clazz = Class.forName("org.napile.vm.invoke.impl.nativeimpl.classes." + parentFq.getFqName().replace(".", "_"));

				nativeMethodRefs = register(clazz, classInfo);
			}
			catch(ClassNotFoundException e)
			{
				return null;
			}
		}

		for(NativeMethodRef methodInfo : nativeMethodRefs)
		{
			if(!methodInfo.getName().equals(name))
				continue;

			if(!Comparing.equal(methodInfo.getParameters(), params))
				continue;

			return methodInfo;
		}

		return null;
	}
}
