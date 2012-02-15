package org.napile.vm.objects.objectinfo.impl.primitive;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ValueObjectInfo;

/**
 * @author VISTALL
 * @date 18:28/15.02.2012
 */
public class ByteObjectInfo extends ValueObjectInfo<Byte>
{
	public ByteObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, Byte value)
	{
		super(classObjectInfo, classInfo, value);
	}
}
