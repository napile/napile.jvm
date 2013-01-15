package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 16:59/29.11.12
 */
public class napile_lang_Char
{
	@NativeImplement(methodName = "equals", parameters = {"napile.lang.Any?"})
	public static BaseObjectInfo equals(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, objectInfo.value().equals(arg[0].value()));
	}

	@NativeImplement(methodName = "toByte", parameters = {})
	public static BaseObjectInfo toByte(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		Character c = objectInfo.value();
		return VmUtil.convertToVm(vm, context, (byte)c.charValue());
	}
}
