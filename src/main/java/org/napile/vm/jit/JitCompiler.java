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

package org.napile.vm.jit;

import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.jit.impl.replacer.NewObjectJitInstructionReplacer;

/**
 * @author VISTALL
 * @since 13:26/03.02.13
 */
public class JitCompiler
{
	public static final JitCompiler INSTANCE = new JitCompiler();

	private JitInstructionReplacer<?>[] replacers = new JitInstructionReplacer[]
	{
		new NewObjectJitInstructionReplacer()
	};

	private JitCompiler()
	{
		//
	}

	public VmInstruction[] processInstructions(final VmInstruction[] instructions)
	{
		VmInstruction[] newInstructions = instructions;

		for(int i = 0; i < instructions.length; i++)
		{
			VmInstruction<?> instruction = instructions[i];

			newInstructions[i] = processReplacers(instruction);
		}

		return newInstructions;
	}

	private VmInstruction<?> processReplacers(VmInstruction<?> instruction)
	{
		for(JitInstructionReplacer<?> instructionReplacer : replacers)
		{
			if(instruction.getClass() == instructionReplacer.getInstructionClass())
				return instructionReplacer.replace((VmInstruction)instruction);
		}

		return instruction;
	}
}
