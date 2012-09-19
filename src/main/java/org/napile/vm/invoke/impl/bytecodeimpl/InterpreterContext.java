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

import org.napile.vm.objects.BaseObjectInfo;

/**
 * @author VISTALL
 * @date 19:56/15.02.2012
 */
public class InterpreterContext
{
	private Deque<StackEntry> _stack = new ArrayDeque<StackEntry>();

	private Deque<BaseObjectInfo> _values = new ArrayDeque<BaseObjectInfo>(2);

	public InterpreterContext(StackEntry... methodInfo)
	{
		for(StackEntry d : methodInfo)
			_stack.add(d);
	}

	public StackEntry getLastStack()
	{
		return _stack.peekLast();
	}

	public void push(BaseObjectInfo val)
	{
		StackEntry entry = getLastStack();
		//StringWriter stringWriter = new StringWriter();
		//new Exception().printStackTrace(new PrintWriter(stringWriter));

		entry.getDebug().add("push: " + val + ": " + entry.getMethodInfo());
		_values.add(val);
	}

	public BaseObjectInfo last()
	{
		BaseObjectInfo v = _values.pollLast();

		//StringWriter stringWriter = new StringWriter();
		//new Exception().printStackTrace(new PrintWriter(stringWriter));

		StackEntry entry = getLastStack();
		entry.getDebug().add("last: " + v + ": " + entry.getMethodInfo());
		return v;
	}

	public Deque<StackEntry> getStack()
	{
		return _stack;
	}
}
