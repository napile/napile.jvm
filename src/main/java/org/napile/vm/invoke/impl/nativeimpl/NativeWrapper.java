package org.napile.vm.invoke.impl.nativeimpl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asmNew.parsing.type.TypeNodeUtil;
import org.napile.asmNew.util.Comparing2;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.compiler.lang.resolve.name.Name;
import org.napile.vm.invoke.impl.nativeimpl.classes.Console;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;
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
		register(vm, Console.class);
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

			AssertUtil.assertTrue(method.getParameterTypes().length != 3 || method.getParameterTypes()[0] != Vm.class || method.getParameterTypes()[1] != ObjectInfo.class || method.getParameterTypes()[2] != ObjectInfo[].class);

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
