package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.asm.lib.NapileLangPackage;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 23:32/20.09.12
 */
public class napile_io_NativeConsole
{
	@NativeImplement(methodName = "write", parameters = {"napile.lang.String"})
	public static void write(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo val = context.getLastStack().getArguments()[0];
		//TODO [VISTALL] remove this hack
		if(val.getClassInfo().getFqName().equals(NapileLangPackage.NULL))
			System.out.println("null");
		else
			System.out.print(VmUtil.convertToJava(vm, val));
	}

	@NativeImplement(methodName = "writeLine", parameters = {"napile.lang.String"})
	public static void writeLine(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo val = context.getLastStack().getArguments()[0];
		//TODO [VISTALL] remove this hack
		if(val.getClassInfo().getFqName().equals(NapileLangPackage.NULL))
			System.out.println("null");
		else
			System.out.println(VmUtil.convertToJava(vm, val));
	}
}
