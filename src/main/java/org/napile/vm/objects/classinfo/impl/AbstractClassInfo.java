package org.napile.vm.objects.classinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 19:02/15.02.2012
 */
public abstract class AbstractClassInfo implements ClassInfo
{
	private ObjectInfo _nullValue;

	public void setNullValue(ObjectInfo nullValue)
	{
		_nullValue = nullValue;
	}

	@Override
	public final ObjectInfo nullValue()
	{
		return _nullValue;
	}

	@Override
	public ConstantPool getConstantPool()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
