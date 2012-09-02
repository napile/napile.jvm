package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 0:16/01.09.12
 */
public class Console
{
	@NativeImplement(className = "Console", methodName = "write", parameters = "napile.lang.Int")
	public static void write(Vm vm, ObjectInfo objectInfo, ObjectInfo[] arg)
	{
		System.out.println("Console.write: " + arg[0]);
	}
}
