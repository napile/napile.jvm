package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.asm.lib.NapileConditionPackage;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @since 18:58/14.01.13
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

	@NativeImplement(methodName = "compareTo", parameters = {"napile.lang.Long"})
	public static BaseObjectInfo compareTo(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		long x = (Long) objectInfo.value();
		long y = (Long)arg[0].value();

		String name;
		if(x == y)
			name = "EQUAL";
		else if(x > y)
			name = "GREATER";
		else
			name = "LOWER";

		return VmUtil.staticValue(vm, NapileConditionPackage.COMPARE_RESULT, name);
	}

	@NativeImplement(methodName = "compareTo", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo compareTo0(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		long x = (Long) objectInfo.value();
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

}
