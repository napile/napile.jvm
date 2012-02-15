package org.napile.jvm.objects.classinfo.impl;

import org.napile.jvm.bytecode.Instruction;
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
	private Instruction[] _instructions;

	private int _maxStack;
	private int _maxLocals;

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

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append(_returnType.getName()).append(" ");
		b.append(getName()).append("(");
		for(int i = 0; i < _parameters.length; i++)
		{
			b.append(_parameters[i].getName());
			if(i != (_parameters.length - 1))
				b.append(", ");
		}
		b.append(")") ;
		return b.toString();
	}

	@Override
	public Instruction[] getInstructions()
	{
		return _instructions;
	}

    public void setInstructions(Instruction[] instructions)
	{
		_instructions = instructions;
	}

	public int getMaxStack()
	{
		return _maxStack;
	}

    public void setMaxStack(int maxStack)
	{
		_maxStack = maxStack;
	}

    public int getMaxLocals()
	{
		return _maxLocals;
	}

    public void setMaxLocals(int maxLocals)
	{
		_maxLocals = maxLocals;
	}
}
