package org.napile.jvm.objects.classinfo.parsing.constantpool;

import java.util.HashMap;
import java.util.Map;

import org.napile.jvm.objects.classinfo.parsing.constantpool.value.ConstantValue;

/**
 * @author VISTALL
 * @date 16:08/31.01.2012
 */
public class ConstantPool
{
	private final Map<Integer, ConstantValue> _constantValues;

	public ConstantPool(int size)
	{
		_constantValues = new HashMap<Integer, ConstantValue>(size);
	}
}
