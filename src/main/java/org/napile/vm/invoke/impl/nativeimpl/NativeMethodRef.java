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

package org.napile.vm.invoke.impl.nativeimpl;

import java.lang.reflect.Method;
import java.util.List;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.compiler.lang.resolve.name.FqName;

/**
 * @author VISTALL
 * @date 0:27/17.02.2012
 */
public class NativeMethodRef
{
	private FqName _name;
	private List<TypeNode> _parameters;
	private Method _method;

	public NativeMethodRef(FqName name, List<TypeNode> params, Method method)
	{
		_name = name;
		_parameters = params;
		_method = method;
	}

	public FqName getName()
	{
		return _name;
	}

	public List<TypeNode> getParameters()
	{
		return _parameters;
	}

	public Method getMethod()
	{
		return _method;
	}
}
