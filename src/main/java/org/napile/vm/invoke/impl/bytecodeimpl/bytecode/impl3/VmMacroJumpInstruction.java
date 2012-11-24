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
import org.napile.asm.tree.members.bytecode.impl.MacroJumpInstruction;
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
 * @date 13:04/24.11.12
 */
public class VmMacroJumpInstruction extends VmInstruction<MacroJumpInstruction>
{
	private FqName className;
	private String methodName;
	private TypeNode[] parameters;

	public VmMacroJumpInstruction(MacroJumpInstruction instruction)
	{
		super(instruction);

		className = instruction.methodRef.method.parent();
		methodName = instruction.methodRef.method.shortName().getName();
		parameters = instruction.methodRef.parameters.toArray(new TypeNode[instruction.methodRef.parameters.size()]);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		ClassInfo classInfo = vm.safeGetClass(className);

		MethodInfo methodInfo = vm.getAnyMethod(classInfo, methodName, true, parameters);

		AssertUtil.assertFalse(methodInfo != null, "Method not found " + methodName + " " + className + " parameters " + StringUtil.join(instruction.methodRef.parameters, ", "));

		BaseObjectInfo[] arguments = new BaseObjectInfo[methodInfo.getParameters().length];
		for(int i = 0; i < methodInfo.getParameters().length; i++)
			arguments[i] = context.pop();

		arguments = ArrayUtil.reverseArray(arguments);

		BaseObjectInfo objectInfo = context.pop();

		StackEntry nextEntry = new StackEntry(objectInfo, methodInfo, arguments, instruction.methodRef.typeArguments);

		context.getStack().add(nextEntry);

		vm.invoke(context);

		StackEntry stackEntry = context.getStack().pollLast();
		if(stackEntry == null)
			return -1;

		StackEntry prevEntry = context.getLastStack();

		BaseObjectInfo returnValue = stackEntry.getReturnValue(true);
		if(returnValue != null)
		{
			prevEntry.setReturnValue(returnValue);
			prevEntry.setForceIndex(BREAK_INDEX);
			return BREAK_INDEX;
		}

		int forceIndex = stackEntry.getForceIndex();
		return forceIndex == -2 ? nextIndex : forceIndex;
	}
}
