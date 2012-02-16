package org.napile.vm.objects.classinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 0:09/04.02.2012
 */
public class PrimitiveClassInfoImpl extends AbstractClassInfo
{
	private String _name;

	public PrimitiveClassInfoImpl(String name)
	{
		_name = name;
	}

	@Override
	public ClassInfo getParent()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return _name;
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
