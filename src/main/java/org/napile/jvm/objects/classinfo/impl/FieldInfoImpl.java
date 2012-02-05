package org.napile.jvm.objects.classinfo.impl;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;

/**
 * @author VISTALL
 * @date 3:03/02.02.2012
 */
public class FieldInfoImpl implements FieldInfo
{
	private ClassInfo _type;
	private short _flags;
	private String _name;

	private Object _value;

	public FieldInfoImpl(ClassInfo type, String name, short flags)
	{
		_type = type;
		_name = name;
		_flags = flags;
	}

	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public int getFlags()
	{
		return _flags;
	}

	@Override
	public Object getValue()
	{
		return _value;
	}

	public void setValue(Object value)
	{
		_value = value;
	}
}
