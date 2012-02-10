package org.napile.jvm.vm;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.impl.PrimitiveClassInfoImpl;
import org.napile.jvm.util.AssertUtil;

/**
 * @author VISTALL
 * @date 18:27/31.01.2012
 */
public class VmUtil
{
	public static void initBootStrap(VmInterface vmInterface)
	{
		makePrimitiveType(VmInterface.PRIMITIVE_VOID, vmInterface);
		makePrimitiveType(VmInterface.PRIMITIVE_BOOLEAN, vmInterface);
		makePrimitiveType(VmInterface.PRIMITIVE_BYTE, vmInterface);
		makePrimitiveType(VmInterface.PRIMITIVE_SHORT, vmInterface);
		makePrimitiveType(VmInterface.PRIMITIVE_INT, vmInterface);
		makePrimitiveType(VmInterface.PRIMITIVE_LONG, vmInterface);
		makePrimitiveType(VmInterface.PRIMITIVE_FLOAT, vmInterface);
		makePrimitiveType(VmInterface.PRIMITIVE_DOUBLE, vmInterface);
		makePrimitiveType(VmInterface.PRIMITIVE_CHAR, vmInterface);

		AssertUtil.assertNull(vmInterface.getClass("java.lang.Object"));
		AssertUtil.assertNull(vmInterface.getClass("java.io.Serializable"));
		//
		AssertUtil.assertNull(vmInterface.getClass("java.lang.Comparable"));
		// for string
		AssertUtil.assertNull(vmInterface.getClass("java.lang.CharSequence"));
		AssertUtil.assertNull(vmInterface.getClass("java.lang.String"));
		// exceptions
		AssertUtil.assertNull(vmInterface.getClass("java.lang.Throwable"));
		AssertUtil.assertNull(vmInterface.getClass("java.lang.Exception"));
		AssertUtil.assertNull(vmInterface.getClass("java.lang.ClassNotFoundException"));
	}

	public static void makePrimitiveType(String name, VmInterface vmInterface)
	{
		ClassInfo classInfo = new PrimitiveClassInfoImpl(name);

		vmInterface.getBootClassLoader().addClassInfo(classInfo);
	}

	public static boolean isSupported(int major, int minor)
	{
		return major >= 43 && minor >= 0;
	}
}
