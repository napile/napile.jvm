package org.napile.jvm.objects.classinfo.impl;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 3:03/02.02.2012
 */
public class MethodInfoImpl implements MethodInfo
{
	private final ClassInfo _returnType;
	private final ClassInfo[] _parameters;
	private short _flags;
	private String _name;

	private ClassInfo[] _throwExceptions;

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

	@Override
	public int getFlags()
	{
		return _flags;
	}

	public void setThrowExceptions(ClassInfo[] throwsClassInfo)
	{
		_throwExceptions = throwsClassInfo;
	}

	@Override
	public ClassInfo[] getThrowExceptions()
	{
		return _throwExceptions;
	}

	@Override
	public ClassInfo getReturnType()
	{
		return _returnType;
	}

	@Override
	public ClassInfo[] getParameters()
	{
		return _parameters;
	}
}
