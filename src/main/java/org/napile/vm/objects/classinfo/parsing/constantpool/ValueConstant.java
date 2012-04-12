package org.napile.vm.objects.classinfo.parsing.constantpool;

/**
 * @author VISTALL
 * @date 3:34/02.02.2012
 */
public abstract class ValueConstant<T> extends Constant
{
	private T _value;

	public ValueConstant(T value)
	{
		_value = value;
	}

	public T getValue()
	{
		return _value;
	}

	@Override
	public String toString()
	{
		return getClass().getName() + ":" + _value;
	}
}