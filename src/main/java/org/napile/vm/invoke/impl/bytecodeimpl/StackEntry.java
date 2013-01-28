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

package org.napile.vm.invoke.impl.bytecodeimpl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.CodeInfo;
import org.napile.asm.tree.members.MethodNode;
import org.napile.asm.tree.members.TypeParameterNode;
import org.napile.asm.tree.members.bytecode.tryCatch.TryCatchBlockNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.localVariable.LocalVariable;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.localVariable.SimpleLocalVariable;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.AssertUtil;

/**
 * @author VISTALL
 * @date 21:09/15.02.2012
 */
public class StackEntry
{
	private static final Logger LOGGER = Logger.getLogger(StackEntry.class);
	private BaseObjectInfo objectInfo;
	@Deprecated
	private MethodInfo methodInfo;

	private final BaseObjectInfo[] arguments;

	private LocalVariable[] localVariables;

	// debug
	private final List<String> debug = new ArrayList<String>();

	// stack
	private final Deque<BaseObjectInfo> stack = new ArrayDeque<BaseObjectInfo>();

	private final Collection<TryCatchBlockNode> tryCatchBlockNodes;

	private BaseObjectInfo[] returnValues;

	private int forceIndex = -2;

	// type parameters
	private final Map<String, TypeNode> typeArguments = new HashMap<String, TypeNode>();

	// for annotations & anonym invoke
	public StackEntry(int maxLocals, BaseObjectInfo[] arguments, Collection<TryCatchBlockNode> tryCatchBlockNodes)
	{
		this.arguments = arguments;

		this.tryCatchBlockNodes = tryCatchBlockNodes;

		initLocalVariables(maxLocals);

		for(int i = 0; i < arguments.length; i++)
			setValue(i, arguments[i]);
	}

	public StackEntry(BaseObjectInfo objectInfo, MethodInfo methodInfo, BaseObjectInfo[] arguments, List<TypeNode> typeArguments)
	{
		this.objectInfo = objectInfo;
		this.methodInfo = methodInfo;
		this.arguments = arguments;

		//FIXME [VISTALL] stupied hack
		if(!methodInfo.getMethodNode().name.equals(MethodNode.CONSTRUCTOR_NAME) && !methodInfo.getMethodNode().name.equals(MethodNode.STATIC_CONSTRUCTOR_NAME))
		{
			AssertUtil.assertFalse(methodInfo.getTypeParameters().size() == typeArguments.size(), methodInfo.toString() + " " + methodInfo.getTypeParameters().size() + " != " + typeArguments.size());

			Iterator<TypeParameterNode> it1 = methodInfo.getTypeParameters().iterator();
			Iterator<TypeNode> it2 = typeArguments.iterator();

			while(it1.hasNext() && it2.hasNext())
				this.typeArguments.put(it1.next().name.getName(), it2.next());
		}

		CodeInfo codeInfo = methodInfo.getMethodNode().code;
		if(codeInfo != null)
			tryCatchBlockNodes = codeInfo.tryCatchBlockNodes;
		else
			tryCatchBlockNodes = Collections.emptyList();

		initLocalVariables(methodInfo.getInvokeType().getMaxLocals());

		if(methodInfo.getInvokeType().getMaxLocals() > 0)
		{
			int i = 0;

			// if is not static - set 'this' to object
			if(objectInfo != null)
				setValue(i++, objectInfo);

			for(BaseObjectInfo arg : arguments)
				setValue(i++, arg);
		}
	}

	private void initLocalVariables(int size)
	{
		localVariables = size == 0 ? LocalVariable.EMPTY_ARRAY : new LocalVariable[size];
		for(int i = 0; i < size; i++)
			localVariables[i] = new SimpleLocalVariable();
	}

	public void push(BaseObjectInfo val)
	{
		debug.add("push: " + val);

		stack.add(val);
	}

	public BaseObjectInfo pop()
	{
		BaseObjectInfo val = stack.pollLast();

		debug.add("pop: " + val);

		return val;
	}

	public void setValue(int index, BaseObjectInfo objectInfo)
	{
		localVariables[index].set(objectInfo);
	}

	public void set(int index, @NotNull LocalVariable localVariable)
	{
		localVariables[index] = localVariable;
	}

	public BaseObjectInfo getValue(int index)
	{
		return localVariables[index].get();
	}

	@NotNull
	public LocalVariable get(int index)
	{
		return localVariables[index];
	}

	public TypeNode getTypeParameterValue(@NotNull String str)
	{
		TypeNode typeNode = typeArguments.get(str);
		if(typeNode != null)
			return typeNode;
		return objectInfo == null ? null : objectInfo.getTypeArguments().get(str);
	}

	public BaseObjectInfo getObjectInfo()
	{
		return objectInfo;
	}

	public BaseObjectInfo[] getArguments()
	{
		return arguments;
	}

	@Deprecated
	public MethodInfo getMethodInfo()
	{
		return methodInfo;
	}

	public List<String> getDebug()
	{
		return debug;
	}

	@Override
	public String toString()
	{
		return methodInfo == null ? "null" : methodInfo.toString();
	}

	public void initReturnValues(int count)
	{
		returnValues = new BaseObjectInfo[count];
	}

	public void setReturnValue(int index, BaseObjectInfo value)
	{
		returnValues[index] = value;
	}

	public BaseObjectInfo[] getReturnValues(boolean macro)
	{
		if(!macro && returnValues == null)
			LOGGER.error("return value cant be null: " + getMethodInfo(), new Exception());

		return returnValues;
	}

	public int getForceIndex()
	{
		return forceIndex;
	}

	public void setForceIndex(int forceIndex)
	{
		this.forceIndex = forceIndex;
	}

	public Collection<TryCatchBlockNode> getTryCatchBlockNodes()
	{
		return tryCatchBlockNodes;
	}
}
