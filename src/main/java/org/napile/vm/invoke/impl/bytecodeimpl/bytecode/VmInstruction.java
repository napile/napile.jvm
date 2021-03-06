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

package org.napile.vm.invoke.impl.bytecodeimpl.bytecode;

import org.jetbrains.annotations.NotNull;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @since 10:16/05.02.2012
 */
public abstract class VmInstruction<E extends org.napile.asm.tree.members.bytecode.Instruction>
{
	public static final int BREAK_INDEX = -1;

	public static final VmInstruction[] EMPTY_ARRAY = new VmInstruction[0];

	public MethodInfo parent;

	private int index;

	@NotNull
	public final E instruction;

	public VmInstruction(@NotNull E instruction)
	{
		this.instruction = instruction;
	}

	public abstract int call(Vm vm, InterpreterContext context, int nextIndex);

	public void setIndex(int index)
	{
		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}

	@Override
	public String toString()
	{
		return "[i" + index + "]: " + instruction;
	}
}
