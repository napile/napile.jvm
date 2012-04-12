package org.napile.vm.objects.objectinfo.impl.primitive;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.impl.ValueObjectInfo;

/**
 * @author VISTALL
 * @date 18:50/15.02.2012
 */
public class LongObjectInfo extends ValueObjectInfo<Long>
{
	public LongObjectInfo(ClassInfo classInfo, Long value)
	{
		super(classInfo, value);
	}
}
