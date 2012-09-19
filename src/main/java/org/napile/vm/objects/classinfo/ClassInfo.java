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

package org.napile.vm.objects.classinfo;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.compiler.lang.resolve.name.FqName;

/**
 * @author VISTALL
 * @date 16:01/31.01.2012
 */
public class ClassInfo implements ReflectInfo
{
	private final List<VariableInfo> variableInfos = new ArrayList<VariableInfo>(0);
	private final List<MethodInfo> methodInfos = new ArrayList<MethodInfo>(0);

	private final List<TypeNode> _extends = new ArrayList<TypeNode>(0);
	private final FqName _name;
	private final ArrayList<Modifier> flags = new ArrayList<Modifier>(0);

	private boolean _staticConstructorCalled;

	public ClassInfo(FqName name)
	{
		_name = name;
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
	public List<VariableInfo> getVariables()
	{
		return variableInfos;
	}

	@NotNull
	public List<TypeNode> getExtends()
	{
		return _extends;
	}

	@NotNull
	public List<MethodInfo> getMethods()
	{
		return methodInfos;
	}

	@Override
	public ClassInfo getParent()
	{
		return null;
	}

	public boolean isStaticConstructorCalled()
	{
		return _staticConstructorCalled;
	}

	public void setStaticConstructorCalled(boolean staticConstructorCalled)
	{
		_staticConstructorCalled = staticConstructorCalled;
	}

	@Override
	public String toString()
	{
		return getName().toString();
	}
}
