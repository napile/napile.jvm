package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.compiler.lang.rt.NapileLangPackage;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 22:37/07.09.12
 */
public class napile_lang_Int
{
	@NativeImplement(className = "napile.lang.Int", methodName = "plus", parameters = {"napile.lang.Int"})
	public static BaseObjectInfo plus(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		return new BaseObjectInfo(vm, NapileLangPackage.INT).setAttach((Integer) objectInfo.getAttach() + ((Integer)arg[0].getAttach()));
	}
}
