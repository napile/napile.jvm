package org.napile.vm.interpreter;

import java.util.ArrayDeque;
import java.util.Deque;

import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 19:56/15.02.2012
 */
public class InterpreterContext
{
	private Deque<WorkData> _stack = new ArrayDeque<WorkData>();

	private Deque<ObjectInfo> _values = new ArrayDeque<ObjectInfo>(2);

	public InterpreterContext(WorkData... methodInfo)
	{
		for(WorkData d : methodInfo)
			_stack.add(d);
	}

	public WorkData getLastWork()
	{
		return _stack.peekLast();
	}

	public void push(ObjectInfo val)
	{
		_values.add(val);
	}

	public ObjectInfo pop()
	{
		return _values.pop();
	}
}
