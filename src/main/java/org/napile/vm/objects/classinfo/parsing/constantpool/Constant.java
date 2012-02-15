package org.napile.vm.objects.classinfo.parsing.constantpool;

/**
 * @author VISTALL
 * @date 16:09/31.01.2012
 */
public abstract class Constant
{
	private int _type;

	public int getType()
	{
		return _type;
	}

	public void setType(int type)
	{
		_type = type;
	}

	@Override
	public abstract String toString();
}
