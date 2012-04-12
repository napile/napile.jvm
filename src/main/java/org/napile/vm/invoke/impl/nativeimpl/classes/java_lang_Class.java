package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 1:27/20.02.2012
 */
public class java_lang_Class
{
	@NativeImplement(className = "java.lang.Class", methodName = "registerNatives", parameters = {})
	public static void registerNatives(Vm vm, ObjectInfo objectInfo, ObjectInfo[] arguments)
	{

	}

	@NativeImplement(className = "java.lang.Class", methodName = "getClassLoader0", parameters = {})
	public static ObjectInfo getClassLoader0(Vm vm, ObjectInfo objectInfo, ObjectInfo[] arguments)
	{
		return VmUtil.OBJECT_NULL;
	}
}
