package org.napile.vm.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.classinfo.parsing.variabletable.LocalVariable;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 21:09/15.02.2012
 */
public class WorkData
{
	private ObjectInfo _objectInfo;
	private MethodInfo _methodInfo;

	private ObjectInfo[] _arguments;

	private final List<ObjectInfo> _localVariables;
	private final LocalVariable[] _parentVariables;

	public WorkData(ObjectInfo objectInfo, MethodInfo methodInfo, ObjectInfo[] arguments)
	{
		_objectInfo = objectInfo;
		_methodInfo = methodInfo;
		_arguments = arguments;

		_parentVariables = methodInfo.getLocalVariables();
		_localVariables = new ArrayList<ObjectInfo>(methodInfo.getLocalVariables().length);
		for(int i = 0; i < _parentVariables.length; i++)
			_localVariables.add(methodInfo.getLocalVariables()[i].getType().nullValue());

		if(!_localVariables.isEmpty())
		{
			int startPos = objectInfo == null ? 0 : 1;

			// if is not static - set 'this' to object
			if(objectInfo != null)
				_localVariables.set(0, objectInfo);

			for(int i = 0; i < arguments.length; i++, startPos ++)
				_localVariables.set(startPos, arguments[i]);
		}
	}

	public ConstantPool getConstantPool()
	{
		return _methodInfo.getParent().getConstantPool();
	}

	public List<ObjectInfo> getLocalVariables()
	{
		return _localVariables;
	}

	public LocalVariable[] getParentVariables()
	{
		return _parentVariables;
	}

	public ObjectInfo getObjectInfo()
	{
		return _objectInfo;
	}

	public ObjectInfo[] getArguments()
	{
		return _arguments;
	}

	public MethodInfo getMethodInfo()
	{
		return _methodInfo;
	}
}
