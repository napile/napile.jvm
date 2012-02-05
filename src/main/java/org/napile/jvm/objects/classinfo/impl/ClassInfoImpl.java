package org.napile.jvm.objects.classinfo.impl;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 3:01/02.02.2012
 */
public class ClassInfoImpl implements ClassInfo
{
	private ClassInfo[] _interfaces = ClassInfo.EMPTY_ARRAY;
	private FieldInfo[] _fields = FieldInfo.EMPTY_ARRAY;
	private MethodInfo[] _methods = MethodInfo.EMPTY_ARRAY;

	private ClassInfo _superClass;
	private final String _name;
	private final int _flags;

	public ClassInfoImpl(String name, int flags)
	{
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
	public FieldInfo[] getFields()
	{
		return _fields;
	}

	@Override
	public ClassInfo getSuperClass()
	{
		return _superClass;
	}

	@Override
	public ClassInfo[] getInterfaces()
	{
		return _interfaces;
	}

	@Override
	public MethodInfo[] getMethods()
	{
		return _methods;
	}

	public void setSuperClass(ClassInfo superClass)
	{
		_superClass = superClass;
	}

	public void setInterfaces(ClassInfo[] interfaces)
	{
		_interfaces = interfaces;
	}

	public void setFields(FieldInfo[] fs)
	{
		_fields = fs;
	}

	public void setMethods(MethodInfo[] methods)
	{
		_methods = methods;
	}
}
