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

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.resolve.name.Name;
import org.napile.asm.tree.members.AnnotationNode;
import org.napile.asm.tree.members.CodeInfo;
import org.napile.asm.tree.members.MethodNode;
import org.napile.asm.tree.members.TypeParameterNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.BytecodeInvokeType;
import org.napile.vm.invoke.impl.NativeInvokeType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Function;

/**
 * @author VISTALL
 * @since 16:03/31.01.2012
 */
public class MethodInfo implements ReflectInfo
{
	private final ClassInfo parent;
	private final Name name;
	private final MethodNode methodNode;
	private final TypeNode returnType;

	private final TypeNode[] parameters;

	private InvokeType invokeType;

	public MethodInfo(ClassInfo parentType, Name name, MethodNode likeMethodNode, TypeNode typeNode, TypeNode[] parameters)
	{
		this.parent = parentType;
		this.name = name;
		this.methodNode = likeMethodNode;
		this.returnType = typeNode;
		this.parameters = parameters;

		if(hasModifier(Modifier.NATIVE))
			setInvokeType(NativeInvokeType.INSTANCE);
		else
		{
			BytecodeInvokeType bytecodeInvokeType = new BytecodeInvokeType();
			CodeInfo codeInfo = likeMethodNode.code;
			if(codeInfo != null)
			{
				bytecodeInvokeType.convertInstructions(codeInfo.instructions, this);
				bytecodeInvokeType.setMaxLocals(codeInfo.maxLocals);
			}
			else
				bytecodeInvokeType.setMaxLocals(0);
			setInvokeType(bytecodeInvokeType);
		}
	}

	@NotNull
	@Override
	public FqName getFqName()
	{
		return parent.getFqName().child(name);
	}

	@Override
	public boolean hasModifier(@NotNull Modifier modifier)
	{
		return ArrayUtil.contains(modifier, getMethodNode().modifiers);
	}

	@Override
	public Modifier[] getModifiers()
	{
		return methodNode.modifiers;
	}

	@Override
	public ClassInfo getParent()
	{
		return parent;
	}

	@Override
	public List<AnnotationNode> getAnnotations()
	{
		return methodNode.annotations;
	}

	@NotNull
	public String getName()
	{
		return methodNode.name.getName();
	}

	public TypeNode getReturnType()
	{
		return returnType;
	}

	public TypeNode[] getParameters()
	{
		return parameters;
	}

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		if(hasModifier(Modifier.STATIC))
			b.append("static ");
		b.append("meth ");
		b.append(getFqName()).append("(");
		b.append(StringUtil.join(parameters, new Function<TypeNode, String>()
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

	public List<TypeParameterNode> getTypeParameters()
	{
		return getMethodNode().typeParameters;
	}

	public InvokeType getInvokeType()
	{
		return invokeType;
	}

	public void setInvokeType(InvokeType invokeType)
	{
		this.invokeType = invokeType;
	}

	public MethodNode getMethodNode()
	{
		return methodNode;
	}
}