package org.napile.vm.objects.objectinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 18:50/15.02.2012
 */
public class ShortObjectInfo extends ObjectInfo<Short>
{
	public ShortObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, Short value)
	{
		super(classObjectInfo, classInfo, value);
	}
}
