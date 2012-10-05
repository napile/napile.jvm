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

package org.napile.vm.invoke.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 4:21/06.02.2012
 */
public class BytecodeInvokeType implements InvokeType
{
	private static final Logger LOGGER = Logger.getLogger(BytecodeInvokeType.class);

	private VmInstruction[] _instructions = VmInstruction.EMPTY_ARRAY;

	private int _maxLocals;

	public BytecodeInvokeType()
	{
		//
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		for(int i = 0; i < _instructions.length;)
		{
			VmInstruction instruction = _instructions[i];

			try
			{
				i = instruction.call(vm, context, i + 1);
			}
			catch(Exception e)
			{
				debug(context, i, e);

				System.exit(-1);
			}
		}

		//debug(context, -1, null);
	}

	private void debug(InterpreterContext context, int errorIndex, Exception e)
	{
		LOGGER.info("-----------------------------------");
		LOGGER.info("Stack:");
		List<StackEntry> entries = new ArrayList<StackEntry>(context.getStack());
		Collections.reverse(entries);
		for(StackEntry stackEntry : entries)
			LOGGER.info("\tat " + stackEntry);
		StackEntry entry = context.getLastStack();

		LOGGER.info("-----------------------------------");
		LOGGER.info("Values:");
		for(String d : entry.getDebug())
			LOGGER.info(d);

		LOGGER.info("-----------------------------------");
		LOGGER.info("Instructions:");
		for(int j = 0; j < _instructions.length; j++)
		LOGGER.info("" + _instructions[j].toString() + ((errorIndex == j) ? " ERROR: " + e.getMessage() : ""));
		if(e != null)
			e.printStackTrace();

		LOGGER.info("-----------------------------------");
	}

	public VmInstruction[] getInstructions()
	{
		return _instructions;
	}

	public void setInstructions(VmInstruction[] instructions)
	{
		_instructions = instructions;
	}

	public int getMaxLocals()
	{
		return _maxLocals;
	}

	public void setMaxLocals(int maxLocals)
	{
		_maxLocals = maxLocals;
	}
}
