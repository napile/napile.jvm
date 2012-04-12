package org.napile.vm.objects.objectinfo.impl.primitive;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.impl.ValueObjectInfo;

/**
 * @author VISTALL
 * @date 18:50/15.02.2012
 */
public class DoubleObjectInfo extends ValueObjectInfo<Double>
{
	public DoubleObjectInfo(ClassInfo classInfo, Double value)
	{
		super(classInfo, value);
	}
}
