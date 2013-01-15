package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.asm.lib.NapileConditionPackage;
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
	@NativeImplement(methodName = "inc", parameters = {})
	public static BaseObjectInfo inc(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() + 1);
	}

	@NativeImplement(methodName = "bitNot", parameters = {})
	public static BaseObjectInfo bitNot(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, ~ (Integer) objectInfo.value());
	}

	@NativeImplement(methodName = "dec", parameters = {})
	public static BaseObjectInfo dec(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() - 1);
	}

	@NativeImplement(methodName = "compareTo", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo compareTo(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		int x = (Integer) objectInfo.value();
		int y = (Integer)arg[0].value();

		String name;
		if(x == y)
			name = "EQUAL";
		else if(x > y)
			name = "GREATER";
		else
			name = "LOWER";

		return VmUtil.staticValue(vm, NapileConditionPackage.COMPARE_RESULT, name);
	}

	@NativeImplement(methodName = "plus", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo plus(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() + ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "times", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo times(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() * ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "minus", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo minus(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() - ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "minus", parameters = {})
	public static BaseObjectInfo minus$(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, - (Integer) objectInfo.value());
	}

	@NativeImplement(methodName = "bitXor", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo bitXor(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() ^ ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "bitOr", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo bitOr(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() | ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "bitAnd", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo bitAnd(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() & ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "mod", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo mod(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() % ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "bitShiftLeft", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo bitShiftLeft(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() << ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "bitShiftRight", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo bitShiftRight(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() >> ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "bitShiftRightZ", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo bitShiftRightZ(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, (Integer) objectInfo.value() >>> ((Integer)arg[0].value()));
	}

	@NativeImplement(methodName = "equals", parameters = {"napile.lang.Any?"})
	public static BaseObjectInfo equals(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		return VmUtil.convertToVm(vm, context, objectInfo.value().equals(arg[0].value()));
	}

	@NativeImplement(methodName = "toString", parameters = {})
	public static BaseObjectInfo toString(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return VmUtil.convertToVm(vm, context, objectInfo.value().toString());
	}

	@NativeImplement(methodName = "toByte", parameters = {})
	public static BaseObjectInfo toByte(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		Integer integer = objectInfo.value();
		return VmUtil.convertToVm(vm, context, integer.byteValue());
	}

	@NativeImplement(methodName = "toLong", parameters = {})
	public static BaseObjectInfo toLong(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		Integer integer = objectInfo.value();
		return VmUtil.convertToVm(vm, context, integer.longValue());
	}
}
