package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 18:58/14.01.13
 */
public class napile_lang_Long
{
	@NativeImplement(methodName = "inc", parameters = {})
	public static BaseObjectInfo inc(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, (Long) objectInfo.value() + 1);
	}

	@NativeImplement(methodName = "minus", parameters = {})
	public static BaseObjectInfo minus(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, - (Long) objectInfo.value());
	}

	@NativeImplement(methodName = "toString", parameters = {})
	public static BaseObjectInfo toString(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, objectInfo.value().toString());
	}
}
