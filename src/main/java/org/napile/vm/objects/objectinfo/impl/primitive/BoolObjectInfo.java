package org.napile.vm.objects.objectinfo.impl.primitive;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.impl.ValueObjectInfo;

/**
 * @author VISTALL
 * @date 18:50/15.02.2012
 */
public class BoolObjectInfo extends ValueObjectInfo<Boolean>
{
	public BoolObjectInfo(ClassInfo classInfo, Boolean value)
	{
		super(classInfo, value);
	}
}
