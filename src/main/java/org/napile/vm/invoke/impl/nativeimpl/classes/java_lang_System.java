package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ArrayObjectInfo;
import org.napile.vm.objects.objectinfo.impl.primitive.IntObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 23:53/16.02.2012
 */
public class java_lang_System
{
	@NativeImplement(className = "java.lang.System", methodName = "registerNatives", parameters = {})
	public static void registerNatives(Vm vm, ObjectInfo objectInfo, ObjectInfo[] arguments)
	{

	}

	@NativeImplement(className = "java.lang.System", methodName = "arraycopy", parameters = {"java.lang.Object", "int", "java.lang.Object", "int", "int"})
	public static void arraycopy(Vm vm, ObjectInfo objectInfo, ObjectInfo[] arguments)
	{
		ArrayObjectInfo src = (ArrayObjectInfo)arguments[0];
		IntObjectInfo srcPos = (IntObjectInfo)arguments[1];
		ArrayObjectInfo desc = (ArrayObjectInfo)arguments[2];
		IntObjectInfo descPos = (IntObjectInfo)arguments[3];
		IntObjectInfo length = (IntObjectInfo)arguments[4];

		System.arraycopy(src.getValue(), srcPos.getValue(), desc.getValue(), descPos.getValue(), length.getValue());
	}
}
