package org.napile.jvm.objects.classinfo.parsing.constantpool.value;

/**
 * @author VISTALL
 * @date 3:29/02.02.2012
 */
public class NothingConstant implements Constant
{
	public static final NothingConstant STATIC = new NothingConstant();

	@Override
	public String toString()
	{
		return getClass().getName();
	}
}
