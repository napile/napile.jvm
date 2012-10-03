package org.napile.vm.invoke.impl.nativeimpl.classes;

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
	@NativeImplement(className = "napile.lang.Int", methodName = "plus", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo plus(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		return VmUtil.convertToVm(vm, (Integer) objectInfo.value() + ((Integer)arg[0].value()));
	}

	@NativeImplement(className = "napile.lang.Int", methodName = "equals", parameters = {"napile.lang.Any?"})
	public static BaseObjectInfo equals(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		return VmUtil.convertToVm(vm, objectInfo.value().equals(arg[0].value()));
	}

	@NativeImplement(className = "napile.lang.Int", methodName = "toString", parameters = {})
	public static BaseObjectInfo toString(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		return VmUtil.convertToVm(vm, objectInfo.value().toString());
	}
}
