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
import java.util.Deque;
import java.util.Iterator;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.objects.BaseObjectInfo;

/**
 * @author VISTALL
 * @date 19:56/15.02.2012
 */
public class InterpreterContext
{
	private Deque<StackEntry> entries = new ArrayDeque<StackEntry>();

	public InterpreterContext(StackEntry... methodInfo)
	{
		for(StackEntry d : methodInfo)
			entries.add(d);
	}

	public void push(@NotNull BaseObjectInfo baseObjectInfo)
	{
		StackEntry last = getLastStack();

		last.push(baseObjectInfo);
	}

	@NotNull
	public BaseObjectInfo pop()
	{
		StackEntry last = getLastStack();

		return last.pop();
	}

	public TypeNode searchTypeParameterValue(@NotNull String name)
	{
		Iterator<StackEntry> iterator = entries.descendingIterator();
		while(iterator.hasNext())
		{
			StackEntry stackEntry = iterator.next();

			TypeNode typeNode = stackEntry.getTypeParameterValue(name);
			if(typeNode != null)
				return typeNode;
		}
		return null;
	}

	public StackEntry getLastStack()
	{
		return entries.peekLast();
	}

	public Deque<StackEntry> getStack()
	{
		return entries;
	}
}
