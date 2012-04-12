package org.napile.vm.objects.objectinfo;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.impl.ClassObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 18:41/31.01.2012
 */
public abstract class ObjectInfo
{
	public static final ObjectInfo[] EMPTY_ARRAY = new ObjectInfo[0];

	protected ClassObjectInfo _classObjectInfo; // object for 'java.lang.Class'

	public abstract ClassInfo getClassInfo();

	public ClassObjectInfo getClassObjectInfo(Vm vm)
	{
		if(_classObjectInfo == null)
			_classObjectInfo = vm.getClassObjectInfo(getClassInfo());

		return _classObjectInfo;
	}
}
