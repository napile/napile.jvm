package org.napile.vm.interpreter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.napile.vm.objects.objectinfo.ObjectInfo;
import sun.reflect.Reflection;

/**
 * @author VISTALL
 * @date 19:56/15.02.2012
 */
public class InterpreterContext
{
	private Deque<StackEntry> _stack = new ArrayDeque<StackEntry>();

	private Deque<ObjectInfo> _values = new ArrayDeque<ObjectInfo>(2);

	// debug
	private List<String> _debug = new ArrayList<String>();

	public InterpreterContext(StackEntry... methodInfo)
	{
		for(StackEntry d : methodInfo)
			_stack.add(d);
	}

	public StackEntry getLastStack()
	{
		return _stack.peekLast();
	}

	public void push(ObjectInfo val)
	{
		_debug.add("push: " + val + ": " + Reflection.getCallerClass(2).getSimpleName());
		_values.add(val);
	}

	public ObjectInfo last()
	{
		ObjectInfo v = _values.pollLast();
		_debug.add("last: " + v + ": " + Reflection.getCallerClass(2).getSimpleName());
		return v;
	}

	public Deque<StackEntry> getStack()
	{
		return _stack;
	}

	public List<String> getDebug()
	{
		return _debug;
	}
}
