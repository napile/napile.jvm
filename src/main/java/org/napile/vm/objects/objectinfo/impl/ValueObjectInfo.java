package org.napile.vm.objects.objectinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 23:26/15.02.2012
 */
public class ValueObjectInfo<T> extends ObjectInfo
{
	private final ClassInfo _classInfo;

	private T _value;

	public ValueObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, T value)
	{
		super(classObjectInfo);
		_classInfo = classInfo;
		_value = value;
	}

	@Override
	public ClassInfo getClassInfo()
	{
		return _classInfo;
	}

	public T getValue()
	{
		return _value;
	}

	public void setValue(T value)
	{
		_value = value;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getValue());

		return builder.toString();
	}
}
