package org.napile.vm.invoke.impl.bytecodeimpl;

import org.napile.vm.invoke.impl.BytecodeInvokeType;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.classinfo.parsing.variabletable.LocalVariable;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 21:09/15.02.2012
 */
public class StackEntry
{
	private ObjectInfo _objectInfo;
	private MethodInfo _methodInfo;

	private ObjectInfo[] _arguments;

	private final ObjectInfo[] _localVariables;
	private final LocalVariable[] _parentVariables;

	public StackEntry(ObjectInfo objectInfo, MethodInfo methodInfo, ObjectInfo[] arguments)
	{
		_objectInfo = objectInfo;
		_methodInfo = methodInfo;
		_arguments = arguments;

		if(methodInfo.getInvokeType() instanceof BytecodeInvokeType)
		{
			BytecodeInvokeType bytecodeInvokeType = (BytecodeInvokeType)methodInfo.getInvokeType();
			_parentVariables = bytecodeInvokeType.getLocalVariables();
			_localVariables = new ObjectInfo[bytecodeInvokeType.getMaxLocals()];

			if(_localVariables.length > 0)
			{
				int startPos = objectInfo == null ? 0 : 1;

				// if is not static - set 'this' to object
				if(objectInfo != null)
					set(0, objectInfo);

				for(int i = 0; i < arguments.length; i++, startPos ++)
					set(startPos, arguments[i]);
			}
		}
		else
		{
			_localVariables = ObjectInfo.EMPTY_ARRAY;
			_parentVariables = LocalVariable.EMPTY_ARRAY;
		}
	}

	public ConstantPool getConstantPool()
	{
		return _methodInfo.getParent().getConstantPool();
	}

	public void set(int index, ObjectInfo objectInfo)
	{
		_localVariables[index] = objectInfo;
	}

	public ObjectInfo get(int index)
	{
		return _localVariables[index];
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
