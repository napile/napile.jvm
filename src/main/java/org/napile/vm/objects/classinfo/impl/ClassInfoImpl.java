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

package org.napile.vm.objects.classinfo.impl;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asmNew.Modifier;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 3:01/02.02.2012
 */
public class ClassInfoImpl extends AbstractClassInfo
{
	private final List<FieldInfo> fieldInfos = new ArrayList<FieldInfo>(0);
	private final List<MethodInfo> methodInfos = new ArrayList<MethodInfo>(0);

	private final List<ClassInfo> _extends = new ArrayList<ClassInfo>(0);
	private final FqName _name;
	private final ArrayList<Modifier> flags = new ArrayList<Modifier>(0);

	private boolean _staticConstructorCalled;

	public ClassInfoImpl(FqName name)
	{
		_name = name;

		setNullValue(VmUtil.OBJECT_NULL);
	}

	@NotNull
	@Override
	public FqName getName()
	{
		return _name;
	}

	@NotNull
	@Override
	public List<Modifier> getFlags()
	{
		return flags;
	}

	@NotNull
	@Override
	public List<FieldInfo> getFields()
	{
		return fieldInfos;
	}

	@NotNull
	@Override
	public List<ClassInfo> getExtends()
	{
		return _extends;
	}

	@NotNull
	@Override
	public List<MethodInfo> getMethods()
	{
		return methodInfos;
	}

	@Override
	public ClassInfo getParent()
	{
		return null;
	}

	@Override
	public boolean isStaticConstructorCalled()
	{
		return _staticConstructorCalled;
	}

	@Override
	public void setStaticConstructorCalled(boolean staticConstructorCalled)
	{
		_staticConstructorCalled = staticConstructorCalled;
	}
}
