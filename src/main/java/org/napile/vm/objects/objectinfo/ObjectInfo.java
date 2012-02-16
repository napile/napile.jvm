package org.napile.vm.objects.objectinfo;

import org.napile.vm.objects.classinfo.ClassInfo;

/**
 * @author VISTALL
 * @date 18:41/31.01.2012
 */
public abstract class ObjectInfo
{
	protected ObjectInfo _classObjectInfo; // object for 'java.lang.Class'

	public ObjectInfo(ObjectInfo classObjectInfo)
	{
		_classObjectInfo = classObjectInfo;
	}

	public ClassInfo getClassInfo()
	{
		return null;
	}

	public ObjectInfo getClassObjectInfo()   //TODO [VISTALL] it
	{
		return _classObjectInfo;
	}
}
