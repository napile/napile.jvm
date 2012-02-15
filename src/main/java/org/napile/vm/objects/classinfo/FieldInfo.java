package org.napile.vm.objects.classinfo;

/**
 * @author VISTALL
 * @date 16:03/31.01.2012
 */
public interface FieldInfo extends ReflectInfo
{
	public static final FieldInfo[] EMPTY_ARRAY = new FieldInfo[0];

	Object getValue();

	ClassInfo getType();
}
