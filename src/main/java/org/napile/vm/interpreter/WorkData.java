package org.napile.vm.interpreter;

import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 21:09/15.02.2012
 */
public class WorkData
{
	private ObjectInfo<?>[] _arguments;
	private MethodInfo _methodInfo;

	public WorkData(MethodInfo methodInfo, ObjectInfo<?>[] arguments)
	{
		_methodInfo = methodInfo;
		_arguments = arguments;
	}

	public ConstantPool getConstantPool()
	{
		return _methodInfo.getParent().getConstantPool();
	}
}
