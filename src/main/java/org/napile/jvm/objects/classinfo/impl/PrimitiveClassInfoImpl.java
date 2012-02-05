package org.napile.jvm.objects.classinfo.impl;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 0:09/04.02.2012
 */
public class PrimitiveClassInfoImpl implements ClassInfo
{
	private String _name;

	public PrimitiveClassInfoImpl(String name)
	{
		_name = name;
	}

	@Override
	public String getName()
	{
		return _name;
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
