package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 13:24/15.01.13
 */
public class napile_io_FileDescriptor
{
	@NativeImplement(methodName = "getStdInDescriptor", parameters = {})
	public static BaseObjectInfo getStdInDescriptor(Vm vm, InterpreterContext context)
	{

		return VmUtil.convertToVm(vm, context, Long.valueOf(0));
	}
}
