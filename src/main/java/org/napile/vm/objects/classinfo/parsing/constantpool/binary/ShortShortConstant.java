package org.napile.vm.objects.classinfo.parsing.constantpool.binary;

import org.napile.vm.objects.classinfo.parsing.constantpool.Constant;

/**
 * @author VISTALL
 * @date 17:56/02.02.2012
 */
public class ShortShortConstant extends Constant
{
	private final short _firstShort;
	private final short _secondShort;


	public ShortShortConstant(short firstShort, short secondShort)
	{
		_secondShort = secondShort;
		_firstShort = firstShort;
	}

	public short getFirstShort()
	{
		return _firstShort;
	}

	public short getSecondShort()
	{
		return _secondShort;
	}

	@Override
	public String toString()
	{
		return getClass().getName() + ":" + _firstShort + "/" + _secondShort;
	}
}
