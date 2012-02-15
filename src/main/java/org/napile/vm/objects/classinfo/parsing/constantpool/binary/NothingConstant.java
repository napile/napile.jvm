package org.napile.vm.objects.classinfo.parsing.constantpool.binary;

import org.napile.vm.objects.classinfo.parsing.constantpool.Constant;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;

/**
 * @author VISTALL
 * @date 3:29/02.02.2012
 */
public class NothingConstant extends Constant
{
	public static final NothingConstant STATIC = new NothingConstant();

	private NothingConstant()
	{
		setType(ConstantPool.CP_NOTHING);
	}

	@Override
	public String toString()
	{
		return getClass().getName();
	}
}
