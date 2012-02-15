package org.napile.vm.objects.objectinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 18:50/15.02.2012
 */
public class FloatObjectInfo extends ObjectInfo<Float>
{
	public FloatObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, Float value)
	{
		super(classObjectInfo, classInfo, value);
	}
}
