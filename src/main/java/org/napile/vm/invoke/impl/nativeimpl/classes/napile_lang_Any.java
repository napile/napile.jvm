package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
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
	@NativeImplement(methodName = "hashCode", parameters = {})
	public static BaseObjectInfo hashCode(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		return VmUtil.convertToVm(vm, context, objectInfo.hashCode());
	}

	@NativeImplement(methodName = "fullyEquals", parameters = {"napile.lang.Any?"})
	public static BaseObjectInfo fullyEquals(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo[] arg = context.getLastStack().getArguments();
		return VmUtil.convertToVm(vm, context, objectInfo.hashCode() == arg[0].hashCode());
	}

	@NativeImplement(methodName = "getClass", parameters = {})
	public static BaseObjectInfo getClass(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		return vm.getOrCreateClassObject(context, objectInfo.getClassInfo());
	}

	@NativeImplement(methodName = "getType", parameters = {})
	public static BaseObjectInfo getType(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		BaseObjectInfo typeInfo = objectInfo.getTypeObject();
		if(typeInfo != null)
			return typeInfo;
		else
		{
			typeInfo = vm.createTypeObject(context, objectInfo.getTypeNode());
			objectInfo.setTypeObject(typeInfo);
			return typeInfo;
		}
	}
}
