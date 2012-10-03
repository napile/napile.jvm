package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.asm.lib.NapileLangPackage;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 23:32/20.09.12
 */
public class napile_io_Console
{
	@NativeImplement(className = "napile.io.Console", methodName = "writeLine", parameters = {"napile.lang.String"})
	public static void writeLine(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		BaseObjectInfo val = arg[0];

		BaseObjectInfo baseObjectInfo = val.getVarValue(vm.getField(vm.getClass(NapileLangPackage.STRING), "array", false));
		BaseObjectInfo[] attach = baseObjectInfo.value();
		StringBuilder b = new StringBuilder();
		for(BaseObjectInfo i : attach)
			b.append(i.value());

		System.out.println(b);
	}
}
