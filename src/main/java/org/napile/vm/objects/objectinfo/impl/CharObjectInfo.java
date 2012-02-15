package org.napile.vm.objects.objectinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 18:28/15.02.2012
 */
public class CharObjectInfo extends ObjectInfo<Character>
{
	public CharObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo, Character value)
	{
		super(classObjectInfo, classInfo, value);
	}
}
