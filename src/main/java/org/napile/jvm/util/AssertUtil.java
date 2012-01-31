package org.napile.jvm.util;

/**
 * @author VISTALL
 * @date 18:28/31.01.2012
 */
public class AssertUtil
{
	public static <T> T assertNull(T val)
	{
		if(val == null)
			throw new IllegalArgumentException("Cant be null");
		return val;
	}
}
