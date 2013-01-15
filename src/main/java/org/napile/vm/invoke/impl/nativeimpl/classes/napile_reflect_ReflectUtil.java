package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.asm.resolve.name.FqName;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 20:22/14.01.13
 */
public class napile_reflect_ReflectUtil
{
	@NativeImplement(methodName = "findClass0", parameters = {"napile.lang.String"})
	public static BaseObjectInfo findClass0(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo[] arguments = context.getLastStack().getArguments();

		ClassInfo classInfo = vm.safeGetClass(new FqName((String) VmUtil.convertToJava(vm, arguments[0])));
		return vm.getOrCreateClassObject(context, classInfo);
	}
}
