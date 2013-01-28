/*
 * Copyright 2010-2013 napile.org
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

import org.jetbrains.annotations.NotNull;
import org.napile.asm.AsmConstants;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.tree.members.bytecode.impl.PutAnonymInstruction;
import org.napile.asm.util.IntIntPair;
import org.napile.vm.invoke.impl.BytecodeInvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 20:37/20.01.13
 */
public class VmPutAnonymInstruction extends VmInstruction<PutAnonymInstruction>
{
	public static class AnonymContext
	{
		public BytecodeInvokeType invokeType;

		public StackEntry stackEntry;
	}

	private BytecodeInvokeType invokeType;

	public VmPutAnonymInstruction(@NotNull PutAnonymInstruction instruction)
	{
		super(instruction);

		invokeType = new BytecodeInvokeType();
		invokeType.setMaxLocals(instruction.code.maxLocals);
		invokeType.convertInstructions(instruction.code.instructions);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		StackEntry last = context.getLastStack();

		AnonymContext c = new AnonymContext();
		c.stackEntry = new StackEntry(instruction.code.maxLocals, new BaseObjectInfo[instruction.code.maxLocals], instruction.code.tryCatchBlockNodes);

		for(IntIntPair pair : instruction.require)
		{
			c.stackEntry.set(pair.b, last.get(pair.a));
		}

		BaseObjectInfo objectInfo = new BaseObjectInfo(vm, vm.safeGetClass(NapileLangPackage.NULL), AsmConstants.NULL_TYPE);
		objectInfo.value(c);

		c.invokeType = invokeType;

		context.push(objectInfo);
		return nextIndex;
	}
}
