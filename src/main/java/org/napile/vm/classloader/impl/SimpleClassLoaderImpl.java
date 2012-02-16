package org.napile.vm.classloader.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.napile.vm.classloader.JClassLoader;
import org.napile.vm.objects.classinfo.ClassInfo;

/**
 * @author VISTALL
 * @date 14:55/10.02.2012
 */
public class SimpleClassLoaderImpl implements JClassLoader
{
	private Map<String, ClassInfo> _classes = new ConcurrentHashMap<String, org.napile.vm.objects.classinfo.ClassInfo>();

	private JClassLoader _parent;

	public SimpleClassLoaderImpl(JClassLoader parent)
	{
		_parent = parent;
	}

	@Override
	public void addClassInfo(ClassInfo classInfo)
	{
		_classes.put(classInfo.getName(), classInfo);
	}

	@Override
	public ClassInfo forName(String name)
	{
		ClassInfo classInfo = _classes.get(name);
		if(classInfo != null)
			return classInfo;

		JClassLoader parent = getParent();
		return parent == null ? null : parent.forName(name);
	}

	@Override
	public JClassLoader getParent()
	{
		return _parent;
	}

	@Override
	public Collection<ClassInfo> getLoadedClasses()
	{
		return _classes.values();
	}
}
