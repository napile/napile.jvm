package org.napile.vm.objects.objectinfo.impl.primitive;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.impl.ValueObjectInfo;

/**
 * @author VISTALL
 * @date 18:28/15.02.2012
 */
public class CharObjectInfo extends ValueObjectInfo<Character>
{
	public CharObjectInfo(ClassInfo classInfo, Character value)
	{
		super(classInfo, value);
	}
}
