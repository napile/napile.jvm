package org.napile.jvm.objects.objectinfo;

import java.util.Map;

import org.napile.jvm.objects.classinfo.ClassInfo;

/**
 * @author VISTALL
 * @date 18:41/31.01.2012
 */
public class ObjectInfo
{
	private ClassInfo _classInfo;

	private Map<String, ObjectInfo[]> _fieldValues;

	public ObjectInfo(ClassInfo classInfo)
	{
		_classInfo = classInfo;
	}
}
