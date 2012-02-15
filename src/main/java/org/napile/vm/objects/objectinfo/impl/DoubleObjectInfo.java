package org.napile.vm.objects.objectinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 18:50/15.02.2012
 */
public class DoubleObjectInfo extends ObjectInfo<Double>
{
	public DoubleObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, Double value)
	{
		super(classObjectInfo, classInfo, value);
	}
}
