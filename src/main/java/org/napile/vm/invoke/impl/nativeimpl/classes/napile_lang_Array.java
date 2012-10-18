package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 1:58/20.09.12
 */
public class napile_lang_Array
{
	@NativeImplement(className = "napile.lang.Array", methodName = "init", parameters = {})
	public static void init(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		//BaseObjectInfo[] arg = context.getLastStack().getArguments();

		VariableInfo variableInfo = vm.getField(objectInfo.getClassInfo(), "length", false);

		AssertUtil.assertNull(variableInfo);

		BaseObjectInfo varValue = objectInfo.getVarValue(variableInfo);

		BaseObjectInfo[] values = new BaseObjectInfo[(Integer)varValue.value()];
		for(int i = 0; i < values.length; i++)
			values[i] = VmUtil.convertToVm(vm, context, null);

		objectInfo.value(values);
	}

	@NativeImplement(className = "napile.lang.Array", methodName = "set", parameters = {"napile.lang.Int", ":E:"})
	public static BaseObjectInfo set(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		BaseObjectInfo[] array = objectInfo.value();

		AssertUtil.assertNull(arg[0]);
		AssertUtil.assertNull(arg[1]);

		array[(Integer) arg[0].value()] = arg[1];
		return objectInfo;
	}

	@NativeImplement(className = "napile.lang.Array", methodName = "get", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo get(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();

		BaseObjectInfo[] array = objectInfo.value();

		AssertUtil.assertNull(arg[0]);

		Integer index = arg[0].value();
		if(index >= array.length)
			throw new ArrayIndexOutOfBoundsException();

		return array[index];
	}
}
