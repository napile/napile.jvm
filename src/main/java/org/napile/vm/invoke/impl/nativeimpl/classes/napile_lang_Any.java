package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 21:25/19.09.12
 */
public class napile_lang_Any
{
	@NativeImplement(className = "napile.lang.Any", methodName = "callFromConstructor", parameters = {})
	public static void callFromConstructor(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{

	}
}
