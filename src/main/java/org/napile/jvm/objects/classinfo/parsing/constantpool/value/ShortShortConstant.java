package org.napile.jvm.objects.classinfo.parsing.constantpool.value;

/**
 * @author VISTALL
 * @date 17:56/02.02.2012
 */
public class ShortShortConstant implements Constant
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
