package org.napile.vm.objects.classinfo.impl;

import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.NativeInvokeType;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 3:03/02.02.2012
 */
public class MethodInfoImpl implements MethodInfo
{
	private final ClassInfo _parentType;
	private final ClassInfo _returnType;
	private final ClassInfo[] _parameters;
	private short _flags;
	private String _name;

	private ClassInfo[] _throwExceptions;

	private InvokeType _invokeType;

	public MethodInfoImpl(ClassInfo parentType, ClassInfo returnType, ClassInfo[] parameters, String name, short flags)
	{
		_parentType = parentType;
		_returnType = returnType;
		_parameters = parameters;
		_name = name;
		_flags = flags;

		if(Flags.isNative(this))
			_invokeType = new NativeInvokeType();
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
	public ClassInfo getParent()
	{
		return _parentType;
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

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append(getParent().getName()).append(":");
		b.append(_returnType.getName()).append(" ");
		b.append(getName()).append("(");
		for(int i = 0; i < _parameters.length; i++)
		{
			b.append(_parameters[i].getName());
			if(i != (_parameters.length - 1))
				b.append(", ");
		}
		b.append(")");
		return b.toString();
	}

	@Override
	public InvokeType getInvokeType()
	{
		return _invokeType;
	}

	public void setInvokeType(InvokeType invokeType)
	{
		_invokeType = invokeType;
	}
}
