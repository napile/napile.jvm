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

package org.napile.vm.jit.impl.replacer;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.bytecode.impl.NewObjectInstruction;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3.VmNewObjectInstruction;
import org.napile.vm.jit.JitInstructionReplacer;
import org.napile.vm.jit.impl.instruction.JitNewArrayInstruction;

/**
 * @author VISTALL
 * @date 13:32/03.02.13
 */
public class NewObjectJitInstructionReplacer implements JitInstructionReplacer<NewObjectInstruction>
{
	@NotNull
	@Override
	public VmInstruction<NewObjectInstruction> replace(VmInstruction<NewObjectInstruction> old)
	{
		TypeNode typeNode = old.instruction.value;
		if(!(typeNode.typeConstructorNode instanceof ClassTypeNode))
			return old;

		ClassTypeNode classTypeNode = ((ClassTypeNode) typeNode.typeConstructorNode);
		if(classTypeNode.className.equals(NapileLangPackage.ARRAY))
		{
			if(typeNode.nullable)
				return new JitNewArrayInstruction(old.instruction, null);
			else
			{
				TypeNode argument = typeNode.arguments.get(0);
				if(!(argument.typeConstructorNode instanceof ClassTypeNode))
					return old;

				FqName argumentFq = ((ClassTypeNode) argument.typeConstructorNode).className;
				if(argumentFq.equals(NapileLangPackage.BYTE))
					return new JitNewArrayInstruction(old.instruction, (byte) 0);
				else if(argumentFq.equals(NapileLangPackage.SHORT))
					return new JitNewArrayInstruction(old.instruction, (short) 0);
				else if(argumentFq.equals(NapileLangPackage.INT))
					return new JitNewArrayInstruction(old.instruction, 0);
				else if(argumentFq.equals(NapileLangPackage.LONG))
					return new JitNewArrayInstruction(old.instruction, (long) 0);
				else if(argumentFq.equals(NapileLangPackage.CHAR))
					return new JitNewArrayInstruction(old.instruction, '\0');
			}
		}
		return old;
	}

	@NotNull
	@Override
	public Class<? extends VmInstruction<NewObjectInstruction>> getInstructionClass()
	{
		return VmNewObjectInstruction.class;
	}
}
