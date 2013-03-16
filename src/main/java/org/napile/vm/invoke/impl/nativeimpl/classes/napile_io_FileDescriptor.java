package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.io.IoSupport;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @since 13:24/15.01.13
 */
public class napile_io_FileDescriptor
{
	@NativeImplement(methodName = "getStdInDescriptor", parameters = {})
	public static BaseObjectInfo getStdInDescriptor(Vm vm, InterpreterContext context)
	{
		return VmUtil.convertToVm(vm, context, IoSupport.INSTANCE.register(System.in));
	}

	@NativeImplement(methodName = "getStdOutDescriptor", parameters = {})
	public static BaseObjectInfo getStdOutDescriptor(Vm vm, InterpreterContext context)
	{
		return VmUtil.convertToVm(vm, context, IoSupport.INSTANCE.register(System.out));
	}

	@NativeImplement(methodName = "getStdErrDescriptor", parameters = {})
	public static BaseObjectInfo getStdErrDescriptor(Vm vm, InterpreterContext context)
	{
		return VmUtil.convertToVm(vm, context, IoSupport.INSTANCE.register(System.err));
	}
}
