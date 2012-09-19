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
import org.napile.compiler.lang.resolve.name.Name;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.objects.Flags;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Function;

/**
 * @author VISTALL
 * @date 16:03/31.01.2012
 */
public class MethodInfo implements ReflectInfo
{
	public static final Name CONSTRUCTOR_NAME = Name.identifier("this");
	public static final Name STATIC_CONSTRUCTOR_NAME = Name.identifier("static");

	private List<Modifier> flags = new ArrayList<Modifier>(0);
	private final ClassInfo _parentType;
	private TypeNode returnType;
	private final List<TypeNode> _parameters = new ArrayList<TypeNode>(0);
	private FqName _name;

	private InvokeType invokeType;

	public MethodInfo(ClassInfo parentType, FqName name)
	{
		_parentType = parentType;
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

	@Override
	public ClassInfo getParent()
	{
		return _parentType;
	}

	public TypeNode getReturnType()
	{
		return returnType;
	}

	public List<TypeNode> getParameters()
	{
		return _parameters;
	}

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append(getParent().getName()).append(":");
		if(Flags.isStatic(this))
			b.append("static").append(" ");
		b.append(getName()).append("(");
		b.append(StringUtil.join(_parameters, new Function<TypeNode, String>()
		{
			@Override
			public String fun(TypeNode typeNode)
			{
				return typeNode.toString();
			}
		}, ", "));
		b.append(")");
		b.append(" : ").append(getReturnType()).append(" ");
		return b.toString();
	}

	public InvokeType getInvokeType()
	{
		return invokeType;
	}

	public void setReturnType(TypeNode returnType)
	{
		this.returnType = returnType;
	}

	public void setInvokeType(InvokeType invokeType)
	{
		this.invokeType = invokeType;
	}
}