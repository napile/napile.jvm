package org.napile.jvm.vm;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 17:26/31.01.2012
 */
public interface VmInterface
{
	ClassInfo getClass(String name);

	FieldInfo getField(ClassInfo info, String name);

	MethodInfo getMethod(ClassInfo info, String name, ClassInfo[] param);

	VmContext getVmContext();
}
