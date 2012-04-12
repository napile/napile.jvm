package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 1:42/20.02.2012
 */
public class java_lang_ClassLoader
{
	@NativeImplement(className = "java.lang.ClassLoader", methodName = "registerNatives", parameters = {})
	public static void registerNatives(Vm vm, ObjectInfo objectInfo, ObjectInfo[] arguments)
	{

	}
}
