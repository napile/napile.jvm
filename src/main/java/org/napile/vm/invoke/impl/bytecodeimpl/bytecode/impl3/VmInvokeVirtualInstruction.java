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

package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.bytecode.impl.InvokeVirtualInstruction;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @date 19:55/21.09.12
 */
public class VmInvokeVirtualInstruction extends VmInstruction<InvokeVirtualInstruction>
{
	private FqName className;
	private String methodName;
	private TypeNode[] parameters;

	public VmInvokeVirtualInstruction(InvokeVirtualInstruction instruction)
	{
		super(instruction);

		className = instruction.methodRef.method.parent();
		methodName = instruction.methodRef.method.shortName().getName();
		parameters = instruction.methodRef.parameters.toArray(new TypeNode[instruction.methodRef.parameters.size()]);
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		ClassInfo classInfo = AssertUtil.assertNull(vm.getClass(className));

		MethodInfo methodInfo = vm.getMethod(classInfo, methodName, true, parameters);

		AssertUtil.assertFalse(methodInfo != null, "Method not found " + methodName + " " + className + " parameters " + StringUtil.join(instruction.methodRef.parameters, ", "));

		BaseObjectInfo[] arguments = new BaseObjectInfo[methodInfo.getParameters().length];
		for(int i = 0; i < methodInfo.getParameters().length; i++)
			arguments[i] = context.pop();

		arguments = ArrayUtil.reverseArray(arguments);

		BaseObjectInfo objectInfo = context.pop();

		StackEntry nextEntry = new StackEntry(objectInfo, methodInfo, arguments);

		context.getStack().add(nextEntry);

		vm.invoke(methodInfo, objectInfo, context, BaseObjectInfo.EMPTY_ARRAY);

		StackEntry stackEntry = context.getStack().pollLast();
		if(stackEntry.getReturnValue() != null)
			context.push(stackEntry.getReturnValue());
		//else
		//	System.out.println(stackEntry + " had null value");
	}
}
