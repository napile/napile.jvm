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

package org.napile.vm.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.asm.Modifier;
import org.napile.asm.resolve.name.Name;
import org.napile.asm.tree.members.CodeInfo;
import org.napile.asm.tree.members.TypeParameterNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.BytecodeInvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3.VmNewObjectInstruction;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @since 23:25/15.02.2012
 */
public final class BaseObjectInfo
{
	public static final BaseObjectInfo[] EMPTY_ARRAY = new BaseObjectInfo[0];

	private final Map<VariableInfo, VariableState> variables;

	private final Map<Name, TypeNode> typeArguments = new HashMap<Name, TypeNode>();

	private final ClassInfo classInfo;

	private final TypeNode typeNode;
	private final boolean staticObject;

	private BaseObjectInfo typeObject;

	private Object attach;

	public BaseObjectInfo(@NotNull Vm vm, @NotNull ClassInfo classInfo, @NotNull TypeNode typeNode, boolean staticObject)
	{
		this.classInfo = classInfo;
		this.typeNode = typeNode;
		this.staticObject = staticObject;

		Iterator<TypeParameterNode> it = classInfo.getTypeParameters().iterator();
		Iterator<TypeNode> it2 = typeNode.arguments.iterator();
		while(it.hasNext() && it2.hasNext())
		{
			TypeParameterNode t1 = it.next();
			TypeNode t2 = it2.next();

			typeArguments.put(t1.name, t2);
		}

		List<VariableInfo> variableInfos = staticObject ? classInfo.getVariables() : VmUtil.collectAllFields(vm, classInfo);
		variables = new HashMap<VariableInfo, VariableState>(variableInfos.size());

		for(VariableInfo f : variableInfos)
		{
			if(f.hasModifier(Modifier.STATIC) && !staticObject || !f.hasModifier(Modifier.STATIC) && staticObject)
				continue;

			variables.put(f, new VariableState());
		}
	}

	@NotNull
	public ClassInfo getClassInfo()
	{
		return classInfo;
	}

	public BaseObjectInfo getVarValue(@NotNull VariableInfo variableInfo)
	{
		final VariableState variableState = variables.get(variableInfo);

		if(variableState == null)
		{
			throw new IllegalArgumentException("Object: " + this + " not contains variable: " + variableInfo);
		}

		if(variableState.value == null)
		{
			throw new IllegalArgumentException("Object: " + this + " variable: " + variableInfo + " is not initialized.");
		}
		return variableState.value;
	}

	public boolean hasVar(VariableInfo variableInfo)
	{
		return variables.containsKey(variableInfo);
	}

	public void setVarValue(@NotNull VariableInfo varValue, @NotNull BaseObjectInfo value)
	{
		final VariableState variableState = variables.get(varValue);

		if(variableState == null)
		{
			throw new IllegalArgumentException("Object: " + this + " not contains variable: " + varValue);
		}

		variableState.value = value;
	}

	public void initializeVariables(VmNewObjectInstruction instruction, InterpreterContext context, Vm vm)
	{
		for(Map.Entry<VariableInfo, VariableState> entry : variables.entrySet())
		{
			final VariableInfo key = entry.getKey();
			final VariableState value = entry.getValue();

			if(!value.initialized)
			{
				value.initialized = true;

				final CodeInfo codeInfo = key.getCodeInfo();
				if(codeInfo == null)
				{
					continue;
				}

				StackEntry stackEntry = new StackEntry(codeInfo.maxLocals, staticObject ? BaseObjectInfo.EMPTY_ARRAY : new BaseObjectInfo[] {this}, codeInfo.tryCatchBlockNodes);
				//stackEntry.position = new CallPosition(instruction);

				context.getStack().add(stackEntry);

				BytecodeInvokeType invokeType = new BytecodeInvokeType();
				invokeType.convertInstructions(codeInfo.instructions, null);

				invokeType.call(vm, context);

				context.getStack().remove(stackEntry);

			}
		}
	}

	@Override
	public String toString()
	{
		return "Type: " + typeNode + ", value: " + attach;
	}

	@SuppressWarnings("unchecked")
	public <T> T value()
	{
		return (T) attach;
	}

	public BaseObjectInfo value(@Nullable Object attach)
	{
		this.attach = attach;
		return this;
	}

	@NotNull
	public TypeNode getTypeNode()
	{
		return typeNode;
	}

	public Map<Name, TypeNode> getTypeArguments()
	{
		return typeArguments;
	}

	public BaseObjectInfo getTypeObject()
	{
		return typeObject;
	}

	public void setTypeObject(BaseObjectInfo typeObject)
	{
		this.typeObject = typeObject;
	}
}
