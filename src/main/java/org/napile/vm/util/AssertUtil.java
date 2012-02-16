package org.napile.vm.util;

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
}
