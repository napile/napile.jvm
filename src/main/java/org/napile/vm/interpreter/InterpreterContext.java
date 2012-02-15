package org.napile.vm.interpreter;

import java.util.ArrayDeque;
import java.util.Deque;

import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 19:56/15.02.2012
 */
public class InterpreterContext
{
	private Deque<MethodInfo> _stack = new ArrayDeque<MethodInfo>();

	private ObjectInfo<?> _value;

	public InterpreterContext(MethodInfo methodInfo)
	{
		_stack.add(methodInfo);
	}

	public MethodInfo getLastMethod()
	{
		return _stack.peekLast();
	}
}
