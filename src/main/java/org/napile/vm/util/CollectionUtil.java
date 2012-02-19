package org.napile.vm.util;

import java.util.List;

/**
 * @author VISTALL
 * @date 15:02/16.02.2012
 */
public class CollectionUtil
{
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	public static <T> T safeGet(T[] list, int val)
	{
		return list.length > val ? list[val] : null;
	}

	public static <T> T safeGet(List<T> list, int val)
	{
		return list.size() > val ? list.get(val) : null;
	}
}
