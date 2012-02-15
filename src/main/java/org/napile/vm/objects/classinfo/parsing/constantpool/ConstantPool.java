package org.napile.vm.objects.classinfo.parsing.constantpool;

import org.napile.vm.objects.classinfo.parsing.constantpool.value.Constant;

/**
 * @author VISTALL
 * @date 16:08/31.01.2012
 */
public class ConstantPool
{
	public static final int CP_NOTHING = -1;
	public static final int CP_UTF8 = 1;
	public static final int CP_INTEGER = 3;
	public static final int CP_FLOAT = 4;
	public static final int CP_LONG = 5;
	public static final int CP_DOUBLE = 6;
	public static final int CP_CLASS = 7;
	public static final int CP_STRING = 8;
	public static final int CP_FIELD_DEF = 9;
	public static final int CP_METHOD_DEF = 10;
	public static final int CP_INTERFACE_DEF = 11;
	public static final int CP_NAME = 12;

	private final Constant[] _constants;

	public ConstantPool(int size)
	{
		_constants = new Constant[size];
	}

	public void addConstantPool(int index, Constant value)
	{
		_constants[index] = value;
	}

	public Constant getConstant(int val)
	{
		return _constants[val];
	}
}
