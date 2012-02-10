package org.napile.jvm.classloader;

import java.util.Collection;

import org.napile.jvm.objects.classinfo.ClassInfo;

/**
 * @author VISTALL
 * @date 14:54/10.02.2012
 */
public interface JClassLoader
{
	void addClassInfo(ClassInfo classInfo);

	ClassInfo forName(String name);

	JClassLoader getParent();

	Collection<ClassInfo> getLoadedClasses();
}
