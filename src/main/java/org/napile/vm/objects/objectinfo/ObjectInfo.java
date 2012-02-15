package org.napile.vm.objects.objectinfo;

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

	public ObjectInfo getClassObjectInfo()   //TODO [VISTALL] it
	{
		return _classObjectInfo;
	}
}
