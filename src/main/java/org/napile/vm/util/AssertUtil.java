package org.napile.vm.util;

import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 18:28/31.01.2012
 */
public class AssertUtil
{
	public static void assertTrue(boolean val)
	{
		if(val)
			throw new IllegalArgumentException("Cant be true");
	}

	public static void assertFalse(boolean val)
	{
		if(!val)
			throw new IllegalArgumentException("Cant be false");
	}

	public static <T> T assertNull(T val)
	{
		if(val == null)
			throw new IllegalArgumentException("Cant be null");
		return val;
	}

	public static <T> T assertNotNull(T val)
	{
		if(val != null)
			throw new IllegalArgumentException("Cant be not null");
		return val;
	}

	public static void assertString(String str)
	{
		if(str != null)
			throw new IllegalArgumentException(str);
	}

	public static <T extends ObjectInfo> T assertNull(T objectInfo)
	{
		if(objectInfo.getClassInfo() == null)
			throw new NullPointerException();

		return objectInfo;
	}
}
