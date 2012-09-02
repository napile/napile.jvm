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

package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.objectinfo.impl.BaseObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 16:16/02.09.12
 */
public class invoke_virtual extends invoke
{
	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		StackEntry entry = context.getLastStack();

		BaseObjectInfo objectInfo = context.last();

		if(objectInfo == VmUtil.OBJECT_NULL)
			objectInfo = null;

		ClassInfo classInfo = AssertUtil.assertNull(vm.getClass(className));

		MethodInfo methodInfo = vm.getAnyMethod(classInfo, methodName, true, parameters);

		AssertUtil.assertNull(methodInfo);

		List<BaseObjectInfo> arguments = new ArrayList<BaseObjectInfo>(methodInfo.getParameters().size());
		for(int i = 0; i < methodInfo.getParameters().size(); i++)
			arguments.add(context.last());

		Collections.reverse(arguments);

		StackEntry nextEntry = new StackEntry(objectInfo, methodInfo, arguments.toArray(new BaseObjectInfo[arguments.size()]));

		context.getStack().add(nextEntry);

		vm.invoke(methodInfo, objectInfo, context, BaseObjectInfo.EMPTY_ARRAY);

		context.getStack().pollLast();
	}
}

