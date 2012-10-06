package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.asm.lib.NapileLangPackage;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 23:32/20.09.12
 */
public class napile_io_Console
{
	@NativeImplement(className = "napile.io.Console", methodName = "write", parameters = {"napile.lang.String"})
	public static void write(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo val = context.getLastStack().getArguments()[0];
		//TODO [VISTALL] remove this hack
		if(val.getClassInfo().getName().equals(NapileLangPackage.NULL))
			System.out.println("null");
		else
		{
			BaseObjectInfo baseObjectInfo = val.getVarValue(vm.getField(vm.getClass(NapileLangPackage.STRING), "array", false));
			BaseObjectInfo[] attach = baseObjectInfo.value();
			StringBuilder b = new StringBuilder();
			for(BaseObjectInfo i : attach)
				b.append(i.value());

			System.out.print(b);
		}
	}

	@NativeImplement(className = "napile.io.Console", methodName = "writeLine", parameters = {"napile.lang.String"})
	public static void writeLine(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo val = context.getLastStack().getArguments()[0];
		//TODO [VISTALL] remove this hack
		if(val.getClassInfo().getName().equals(NapileLangPackage.NULL))
			System.out.println("null");
		else
		{
			BaseObjectInfo baseObjectInfo = val.getVarValue(vm.getField(vm.getClass(NapileLangPackage.STRING), "array", false));
			BaseObjectInfo[] attach = baseObjectInfo.value();
			StringBuilder b = new StringBuilder();
			for(BaseObjectInfo i : attach)
				b.append(i.value());

			System.out.println(b);
		}
	}
}
