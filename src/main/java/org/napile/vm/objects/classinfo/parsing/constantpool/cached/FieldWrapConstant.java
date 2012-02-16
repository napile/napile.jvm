package org.napile.vm.objects.classinfo.parsing.constantpool.cached;

import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.Constant;

/**
 * @author VISTALL
 * @date 21:21/15.02.2012
 */
public class FieldWrapConstant extends Constant
{
	private final FieldInfo _fieldInfo;

	public FieldWrapConstant(FieldInfo fieldInfo)
	{
		_fieldInfo = fieldInfo;
	}

	public FieldInfo getFieldInfo()
	{
		return _fieldInfo;
	}

	@Override
	public String toString()
	{
		return _fieldInfo.toString();
	}
}
