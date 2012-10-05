package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 22:37/07.09.12
 */
public class napile_lang_Int
{
	@NativeImplement(className = "napile.lang.Int", methodName = "inc", parameters = {})
	public static BaseObjectInfo inc(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() + 1);
	}

	@NativeImplement(className = "napile.lang.Int", methodName = "plus", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo plus(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() + ((Integer)arg[0].value()));
	}

	@NativeImplement(className = "napile.lang.Int", methodName = "minus", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo minus(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() - ((Integer)arg[0].value()));
	}

	@NativeImplement(className = "napile.lang.Int", methodName = "mod", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo mod(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() % ((Integer)arg[0].value()));
	}

	@NativeImplement(className = "napile.lang.Int", methodName = "equals", parameters = {"napile.lang.Any?"})
	public static BaseObjectInfo equals(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, objectInfo.value().equals(arg[0].value()));
	}

	@NativeImplement(className = "napile.lang.Int", methodName = "toString", parameters = {})
	public static BaseObjectInfo toString(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, objectInfo.value().toString());
	}
}
