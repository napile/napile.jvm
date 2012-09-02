package org.napile.vm.objects;

import org.napile.asmNew.Modifier;
import org.napile.vm.objects.classinfo.ReflectInfo;

/**
 * @author VISTALL
 * @date 9:54/05.02.2012
 */
public class Flags
{
	public static boolean isNative(ReflectInfo reflectInfo)
	{
		return reflectInfo.getFlags().contains(Modifier.NATIVE);
	}

	public static boolean isStatic(ReflectInfo reflectInfo)
	{
		return reflectInfo.getFlags().contains(Modifier.STATIC);
	}
}
