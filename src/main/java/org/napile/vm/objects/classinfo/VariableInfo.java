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

import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.VariableNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.objects.BaseObjectInfo;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @date 16:03/31.01.2012
 */
public class VariableInfo implements ReflectInfo
{
	private final ClassInfo parent;
	private final VariableNode variableNode;

	private BaseObjectInfo staticValue;

	public VariableInfo(ClassInfo parent, VariableNode variableNode)
	{
		this.parent = parent;
		this.variableNode = variableNode;
	}

	@Override
	public ClassInfo getParent()
	{
		return parent;
	}

	@NotNull
	@Override
	public FqName getFqName()
	{
		return parent.getFqName().child(variableNode.name);
	}

	@NotNull
	public String getName()
	{
		return variableNode.name.getName();
	}

	@Override
	public boolean hasModifier(@NotNull Modifier modifier)
	{
		return ArrayUtil.contains(modifier, variableNode.modifiers);
	}

	@Override
	public Modifier[] getModifiers()
	{
		return variableNode.modifiers;
	}

	public void setStaticValue(BaseObjectInfo value)
	{
		staticValue = value;
	}

	public BaseObjectInfo getStaticValue()
	{
		return staticValue;
	}

	public TypeNode getType()
	{
		return variableNode.returnType;
	}

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append(getParent().getFqName()).append(":");
		b.append(getType().toString()).append(" ");
		b.append(getFqName());
		return b.toString();
	}
}
