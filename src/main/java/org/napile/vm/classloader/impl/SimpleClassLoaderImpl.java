/*
 * Copyright 2010-2012 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.vm.classloader.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.classloader.JClassLoader;
import org.napile.vm.objects.classinfo.ClassInfo;

/**
 * @author VISTALL
 * @date 14:55/10.02.2012
 */
public class SimpleClassLoaderImpl implements JClassLoader
{
	private Map<FqName, ClassInfo> _classes = new ConcurrentHashMap<FqName, org.napile.vm.objects.classinfo.ClassInfo>();

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
	public ClassInfo forName(FqName name)
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
