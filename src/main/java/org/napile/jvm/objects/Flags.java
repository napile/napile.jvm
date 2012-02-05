package org.napile.jvm.objects;

import java.lang.reflect.Modifier;

import org.napile.jvm.objects.classinfo.ReflectInfo;

/**
 * @author VISTALL
 * @date 9:54/05.02.2012
 */
public class Flags
{
	public static boolean isStatic(ReflectInfo reflectInfo)
	{
		return Modifier.isStatic(reflectInfo.getFlags());
	}
}
