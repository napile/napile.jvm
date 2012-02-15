package org.napile.vm.objects.objectinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 18:28/15.02.2012
 */
public class ByteObjectInfo extends ObjectInfo<Byte>
{
	public ByteObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, Byte value)
	{
		super(classObjectInfo, classInfo, value);
	}
}
