package org.napile.vm.objects.objectinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 18:50/15.02.2012
 */
public class BoolObjectInfo extends ObjectInfo<Boolean>
{
	public BoolObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, Boolean value)
	{
		super(classObjectInfo, classInfo, value);
	}
}
