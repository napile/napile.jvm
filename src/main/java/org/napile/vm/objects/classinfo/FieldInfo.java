package org.napile.vm.objects.classinfo;

import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 16:03/31.01.2012
 */
public interface FieldInfo extends ReflectInfo
{
	public static final FieldInfo[] EMPTY_ARRAY = new FieldInfo[0];

	void setValue(ObjectInfo value);

	ObjectInfo getValue();

	ClassInfo getType();

	Object getTempValue();

	void setTempValue(Object tempValue);
}
