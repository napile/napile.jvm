package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 1:58/20.09.12
 */
public class napile_lang_Array
{
	@NativeImplement(className = "napile.lang.Array", methodName = "init", parameters = {})
	public static void init(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		VariableInfo variableInfo = vm.getField(objectInfo.getClassInfo(), "length", false);

		AssertUtil.assertNull(variableInfo);

		BaseObjectInfo varValue = objectInfo.getVarValue(variableInfo);

		BaseObjectInfo[] values = new BaseObjectInfo[varValue.value(int.class)];

		objectInfo.setAttach(values);
	}

	@NativeImplement(className = "napile.lang.Array", methodName = "set", parameters = {"napile.lang.Int", ":E:"})
	public static void set(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		BaseObjectInfo[] array = objectInfo.value(BaseObjectInfo[].class);

		AssertUtil.assertNull(arg[0]);
		AssertUtil.assertNull(arg[1]);

		array[arg[0].value(int.class)] = arg[1];
	}

	@NativeImplement(className = "napile.lang.Array", methodName = "get", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo get(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		BaseObjectInfo[] array = objectInfo.value(BaseObjectInfo[].class);

		AssertUtil.assertNull(arg[0]);

		int index = arg[0].value(int.class);
		if(index >= array.length)
			throw new ArrayIndexOutOfBoundsException();

		return array[index];
	}
}
