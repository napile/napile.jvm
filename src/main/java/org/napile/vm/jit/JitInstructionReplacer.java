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

import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;

/**
 * @author VISTALL
 * @since 13:30/03.02.13
 */
public interface JitInstructionReplacer<V extends Instruction>
{
	@NotNull
	public VmInstruction<V> replace(VmInstruction<V> old);

	@NotNull
	public Class<? extends VmInstruction<V>> getInstructionClass();
}
