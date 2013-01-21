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
import org.napile.vm.invoke.impl.BytecodeInvokeType;
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

	private final BaseObjectInfo[] localVariables;

	// debug
	private final List<String> debug = new ArrayList<String>();

	// stack
	private final Deque<BaseObjectInfo> stack = new ArrayDeque<BaseObjectInfo>();

	private final Collection<TryCatchBlockNode> tryCatchBlockNodes;

	private BaseObjectInfo returnValue;

	private int forceIndex = -2;

	// type parameters
	private final Map<String, TypeNode> typeArguments = new HashMap<String, TypeNode>();

	// for annotations & anonym invoke
	public StackEntry(int maxLocals, BaseObjectInfo[] arguments, Collection<TryCatchBlockNode> tryCatchBlockNodes)
	{
		this.arguments = arguments;

		this.tryCatchBlockNodes = tryCatchBlockNodes;

		localVariables = maxLocals == 0 ? BaseObjectInfo.EMPTY_ARRAY : new BaseObjectInfo[maxLocals];

		System.arraycopy(arguments, 0, localVariables, 0, arguments.length);
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

		if(methodInfo.getInvokeType() instanceof BytecodeInvokeType)
		{
			BytecodeInvokeType bytecodeInvokeType = (BytecodeInvokeType)methodInfo.getInvokeType();

			localVariables = new BaseObjectInfo[bytecodeInvokeType.getMaxLocals()];

			if(localVariables.length > 0)
			{
				int i = 0;

				// if is not static - set 'this' to object
				if(objectInfo != null)
					localVariables[i++] = objectInfo;

				for(BaseObjectInfo arg : arguments)
					localVariables[i++] = arg;
			}
		}
		else
			localVariables = BaseObjectInfo.EMPTY_ARRAY;
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

	public void set(int index, BaseObjectInfo objectInfo)
	{
		localVariables[index] = objectInfo;
	}

	public BaseObjectInfo get(int index)
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

	public BaseObjectInfo getReturnValue(boolean macro)
	{
		if(!macro && returnValue == null)
			LOGGER.error("return value cant be null: " + getMethodInfo());
		return returnValue;
	}

	public void setReturnValue(BaseObjectInfo returnValue)
	{
		this.returnValue = returnValue;
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
