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

import java.lang.reflect.Constructor;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.resolve.name.Name;
import org.napile.asm.tree.members.MethodNode;
import org.napile.asm.tree.members.TypeParameterNode;
import org.napile.asm.tree.members.bytecode.Instruction;
import org.napile.asm.tree.members.bytecode.tryCatch.TryCatchBlockNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.BytecodeInvokeType;
import org.napile.vm.invoke.impl.NativeInvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Function;

/**
 * @author VISTALL
 * @date 16:03/31.01.2012
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
			bytecodeInvokeType.setMaxLocals(likeMethodNode.maxLocals);
			setInvokeType(bytecodeInvokeType);

			VmInstruction[] instructions = new VmInstruction[likeMethodNode.instructions.size()];
			bytecodeInvokeType.setInstructions(instructions);

			int i = 0;
			for(Instruction instruction : likeMethodNode.instructions)
			{
				try
				{
					Class<?> clazz = Class.forName("org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3.Vm" + instruction.getClass().getSimpleName());

					Constructor constructor = clazz.getConstructors()[0];

					VmInstruction<?> vmInstruction = (VmInstruction)constructor.newInstance(instruction);
					vmInstruction.setArrayIndex(i++);
					instructions[vmInstruction.getArrayIndex()] = vmInstruction;
				}
				catch(Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}

	@NotNull
	@Override
	public FqName getName()
	{
		return parent.getName().child(name);
	}

	@Override
	public boolean hasModifier(@NotNull Modifier modifier)
	{
		return ArrayUtil.contains(modifier, getMethodNode().modifiers);
	}

	@Override
	public ClassInfo getParent()
	{
		return parent;
	}

	public List<TryCatchBlockNode> getTryCatchBlockNodes()
	{
		return getMethodNode().tryCatchBlockNodes;
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
		b.append(getName()).append("(");
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