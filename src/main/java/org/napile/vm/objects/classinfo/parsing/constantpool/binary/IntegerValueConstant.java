package org.napile.vm.objects.classinfo.parsing.constantpool.binary;

import org.napile.vm.objects.classinfo.parsing.constantpool.ValueConstant;

/**
 * @author VISTALL
 * @date 16:21/02.02.2012
 */
public class IntegerValueConstant extends ValueConstant<Integer>
{
	public IntegerValueConstant(Integer value)
	{
		super(value);
	}
}