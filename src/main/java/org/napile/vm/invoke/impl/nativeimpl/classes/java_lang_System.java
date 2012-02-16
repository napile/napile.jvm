package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ArrayObjectInfo;
import org.napile.vm.objects.objectinfo.impl.primitive.IntObjectInfo;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 23:53/16.02.2012
 */
public class java_lang_System
{
	// public static native void arraycopy(Object src,  int  srcPos, Object dest, int destPos, int length)
	@NativeImplement(className = "java.lang.System", methodName = "arraycopy", parameters = {"java.lang.Object", "int", "java.lang.Object", "int", "int"})
	public static void arraycopy(VmInterface vmInterface, ObjectInfo objectInfo, ObjectInfo[] arguments)
	{
		ArrayObjectInfo src = (ArrayObjectInfo)arguments[0];
		IntObjectInfo srcPos = (IntObjectInfo)arguments[1];
		ArrayObjectInfo desc = (ArrayObjectInfo)arguments[2];
		IntObjectInfo descPos = (IntObjectInfo)arguments[3];
		IntObjectInfo length = (IntObjectInfo)arguments[4];

		System.arraycopy(src.getValue(), srcPos.getValue(), desc.getValue(), descPos.getValue(), length.getValue());
	}
}
