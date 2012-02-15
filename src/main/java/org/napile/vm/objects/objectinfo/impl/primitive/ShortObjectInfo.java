package org.napile.vm.objects.objectinfo.impl.primitive;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ValueObjectInfo;

/**
 * @author VISTALL
 * @date 18:50/15.02.2012
 */
public class ShortObjectInfo extends ValueObjectInfo<Short>
{
	public ShortObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, Short value)
	{
		super(classObjectInfo, classInfo, value);
	}
}
