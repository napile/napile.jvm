package org.napile.jvm.vm;

import org.napile.jvm.util.AssertUtil;

/**
 * @author VISTALL
 * @date 18:27/31.01.2012
 */
public class VmUtil
{
	public static void initBootStrap(VmInterface vmInterface)
	{
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

	public static boolean isSupported(int major, int minor)
	{
		return major >= 43 && minor >= 0;
	}
}
