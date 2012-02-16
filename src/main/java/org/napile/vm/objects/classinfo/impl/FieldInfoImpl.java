package org.napile.vm.objects.classinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 3:03/02.02.2012
 */
public class FieldInfoImpl implements FieldInfo
{
	private ClassInfo _parent;
	private ClassInfo _type;
	private short _flags;
	private String _name;

	private ObjectInfo _value;

	private Object _tempValue;

	public FieldInfoImpl(ClassInfo parent, ClassInfo type, String name, short flags)
	{
		_parent = parent;
		_type = type;
		_name = name;
		_flags = flags;
	}

	@Override
	public ClassInfo getParent()
	{
		return _parent;
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
	public void setValue(ObjectInfo value)
	{
		_value = value;
	}

	@Override
	public ObjectInfo getValue()
	{
		return _value;
	}

	@Override
	public ClassInfo getType()
	{
		return _type;
	}

	public Object getTempValue()
	{
		return _tempValue;
	}

	public void setTempValue(Object tempValue)
	{
		_tempValue = tempValue;
	}
}
