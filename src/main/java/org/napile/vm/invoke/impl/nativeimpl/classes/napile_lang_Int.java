package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.compiler.lang.rt.NapileLangPackage;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.objectinfo.impl.BaseObjectInfo;
import org.napile.vm.objects.objectinfo.impl.IntObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 22:37/07.09.12
 */
public class napile_lang_Int
{
	@NativeImplement(className = "napile.lang.Int", methodName = "plus", parameters = {"napile.lang.Int"})
	public static IntObjectInfo plus(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		return new IntObjectInfo(vm.getClass(NapileLangPackage.INT), ((IntObjectInfo) objectInfo).getValue() + ((IntObjectInfo) arg[0]).getValue());
	}
}
