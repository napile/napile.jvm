package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.invoke.impl.nativeimpl.io.IoSupport;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @since 13:45/15.01.13
 */
public class org_apache_harmony_luni_platform_OSFileSystem
{
	@NativeImplement(methodName = "writeImpl", parameters = {"napile.lang.Long", "napile.lang.Array<napile.lang.Byte>", "napile.lang.Int", "napile.lang.Int"})
	public static BaseObjectInfo writeImpl(Vm vm, InterpreterContext context)
	{
		return IoSupport.INSTANCE.writeOrRead(vm, context, context.getLastStack().getArguments());
	}
}
