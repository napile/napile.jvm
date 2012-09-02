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

import java.util.ArrayList;
import java.util.List;

import org.napile.vm.invoke.impl.BytecodeInvokeType;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.objectinfo.impl.BaseObjectInfo;

/**
 * @author VISTALL
 * @date 21:09/15.02.2012
 */
public class StackEntry
{
	private BaseObjectInfo _objectInfo;
	private MethodInfo _methodInfo;

	private BaseObjectInfo[] _arguments;

	private final BaseObjectInfo[] _localVariables;

	// debug
	private List<String> _debug = new ArrayList<String>();

	public StackEntry(BaseObjectInfo objectInfo, MethodInfo methodInfo, BaseObjectInfo[] arguments)
	{
		_objectInfo = objectInfo;
		_methodInfo = methodInfo;
		_arguments = arguments;

		if(methodInfo.getInvokeType() instanceof BytecodeInvokeType)
		{
			BytecodeInvokeType bytecodeInvokeType = (BytecodeInvokeType)methodInfo.getInvokeType();

			_localVariables = new BaseObjectInfo[bytecodeInvokeType.getMaxLocals()];

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
			_localVariables = BaseObjectInfo.EMPTY_ARRAY;
		}
	}


	public void set(int index, BaseObjectInfo objectInfo)
	{
		_localVariables[index] = objectInfo;
	}

	public BaseObjectInfo get(int index)
	{
		return _localVariables[index];
	}

	public BaseObjectInfo getObjectInfo()
	{
		return _objectInfo;
	}

	public BaseObjectInfo[] getArguments()
	{
		return _arguments;
	}

	public MethodInfo getMethodInfo()
	{
		return _methodInfo;
	}

	public List<String> getDebug()
	{
		return _debug;
	}

	@Override
	public String toString()
	{
		return _methodInfo.toString();
	}
}
