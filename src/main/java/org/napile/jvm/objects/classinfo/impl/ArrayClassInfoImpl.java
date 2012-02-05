package org.napile.jvm.objects.classinfo.impl;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 0:25/04.02.2012
 */
public class ArrayClassInfoImpl implements ClassInfo
{
	private ClassInfo _type;

	public ArrayClassInfoImpl(ClassInfo type)
	{
		_type = type;
	}

	@Override
	public String getName()
	{
		return _type.getName() + "[]";
	}

	@Override
	public int getFlags()
	{
		return 0;
	}

	@Override
	public FieldInfo[] getFields()
	{
		return FieldInfo.EMPTY_ARRAY;
	}

	@Override
	public MethodInfo[] getMethods()
	{
		return MethodInfo.EMPTY_ARRAY;
	}

	@Override
	public ClassInfo getSuperClass()
	{
		return null;
	}

	@Override
	public ClassInfo[] getInterfaces()
	{
		return ClassInfo.EMPTY_ARRAY;
	}
}
