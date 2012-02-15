package org.napile.vm.objects.classinfo.parsing.constantpool.binary;

import org.napile.vm.objects.classinfo.parsing.constantpool.ValueConstant;

/**
 * @author VISTALL
 * @date 3:32/02.02.2012
 */
public class Utf8ValueConstant extends ValueConstant<String>
{
	public Utf8ValueConstant(String value)
	{
		super(value);
	}
}
