package org.napile.jvm.objects.classinfo.impl;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 3:03/02.02.2012
 */
public class MethodInfoImpl implements MethodInfo
{
	private ClassInfo _returnType;
	private ClassInfo[] _parameters;
	private short _flags;
	private String _name;

	public MethodInfoImpl(ClassInfo returnType, ClassInfo[] parameters, String name, short flags)
	{
		_returnType = returnType;
		_parameters = parameters;
		_name = name;
		_flags = flags;
	}

	@Override
	public String getName()
	{
		return _name;
	}
}
