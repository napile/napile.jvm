package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 21:25/19.09.12
 */
public class napile_lang_Any
{
	@NativeImplement(className = "napile.lang.Any", methodName = "fullyEquals", parameters = {"napile.lang.Any?"})
	public static BaseObjectInfo fullyEquals(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		return VmUtil.convertToVm(vm, objectInfo.hashCode() == arg[0].hashCode());
	}
}
