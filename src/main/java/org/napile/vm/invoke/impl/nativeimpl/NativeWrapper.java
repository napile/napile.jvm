package org.napile.vm.invoke.impl.nativeimpl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.napile.vm.invoke.impl.nativeimpl.classes.java_lang_System;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 23:59/16.02.2012
 */
public class NativeWrapper
{
	private static Map<ClassInfo, List<NativeMethod>> WRAPPERS = new HashMap<ClassInfo, List<NativeMethod>>();

	public static void initAll(VmInterface vmInterface)
	{
		register(vmInterface, java_lang_System.class);
	}

	private static void register(VmInterface vmInterface, Class<?> clazz)
	{
		for(Method method : clazz.getDeclaredMethods())
		{
			if(!Modifier.isStatic(method.getModifiers()))
				continue;

			NativeImplement nativeImplement = method.getAnnotation(NativeImplement.class);
			if(nativeImplement == null)
				continue;

			AssertUtil.assertTrue(method.getParameterTypes()[0] != VmInterface.class || method.getParameterTypes()[1] != ObjectInfo.class || method.getParameterTypes()[2] != ObjectInfo[].class);

			ClassInfo classInfo = vmInterface.getClass(nativeImplement.className());

			List<NativeMethod> list = WRAPPERS.get(classInfo);
			if(list == null)
				WRAPPERS.put(classInfo, list = new ArrayList<NativeMethod>());

			ClassInfo[] params = new ClassInfo[nativeImplement.parameters().length];
			for(int i = 0; i < params.length; i++)
				params[i] = vmInterface.getClass(nativeImplement.parameters()[i]);

			NativeMethod methodInfo = new NativeMethod(nativeImplement.methodName(), nativeImplement.parameters(), method);

			list.add(methodInfo);
		}
	}

	public static NativeMethod getMethod(ClassInfo classInfo, String name, ClassInfo[] params)
	{
		List<NativeMethod> nativeMethods = WRAPPERS.get(classInfo);
		if(nativeMethods == null)
			return null;

		for(NativeMethod methodInfo : nativeMethods)
		{
			if(!methodInfo.getName().equals(name))
				continue;

			String[] paramTypes = methodInfo.getParameters();
			if(paramTypes.length != params.length)
				continue;

			loop:
			{
				for(int i = 0; i < params.length; i++)
				{
					if(!paramTypes[i].equals(params[i].getName()))
						break loop;
				}

				return methodInfo;
			}
		}

		return null;
	}
}
