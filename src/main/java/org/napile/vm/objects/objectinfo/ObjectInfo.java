package org.napile.vm.objects.objectinfo;

import org.napile.vm.objects.classinfo.ClassInfo;

/**
 * @author VISTALL
 * @date 18:41/31.01.2012
 */
public class ObjectInfo<T>
{
	private final ObjectInfo _classObjectInfo; // object for 'java.lang.Class'

	private final ClassInfo _classInfo;

	private T _value;

	public ObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, T value)
	{
		_classObjectInfo = classObjectInfo;
		_classInfo = classInfo;
		_value = value;
	}

	public ObjectInfo getClassObjectInfo()
	{
		return _classObjectInfo;
	}

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
}
