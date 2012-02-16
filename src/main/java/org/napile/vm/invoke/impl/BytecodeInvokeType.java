package org.napile.vm.invoke.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.objects.classinfo.parsing.variabletable.LocalVariable;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 4:21/06.02.2012
 */
public class BytecodeInvokeType implements InvokeType
{
	private static final Logger LOGGER = Logger.getLogger(BytecodeInvokeType.class);

	private LocalVariable[] _localVariables = LocalVariable.EMPTY_ARRAY;
	private Instruction[] _instructions;

	private int _maxStack;
	private int _maxLocals;

	public BytecodeInvokeType()
	{
		//
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		for(int i = 0; i < _instructions.length; i++)
		{
			Instruction instruction = _instructions[i];

			try
			{
				instruction.call(vm, context);
			}
			catch(Exception e)
			{
				debug(context, i, e);

				System.exit(-1);
			}
		}

		debug(context, -1, null);
	}

	private void debug(InterpreterContext context, int errorIndex, Exception e)
	{
		LOGGER.info("-----------------------------------");
		LOGGER.info("Stack:");
		List<StackEntry> entries = new ArrayList<StackEntry>(context.getStack());
		Collections.reverse(entries);
		for(StackEntry stackEntry : entries)
			LOGGER.info("> " + stackEntry.getMethodInfo().toString());

		LOGGER.info("-----------------------------------");
		LOGGER.info("Values:");
		for(String d : context.getDebug())
			LOGGER.info(d);

		LOGGER.info("-----------------------------------");
		LOGGER.info("Instructions:");
		for(int j = 0; j < _instructions.length; j++)
		LOGGER.info(j + " [" + _instructions[j].getClass().getSimpleName() + "]" + ((errorIndex == j) ? " ERROR: " + e.getMessage() : ""));
		if(e != null)
			e.printStackTrace();

		LOGGER.info("-----------------------------------");
	}

	public Instruction[] getInstructions()
	{
		return _instructions;
	}

	public LocalVariable[] getLocalVariables()
	{
		return _localVariables;
	}

	public void setInstructions(Instruction[] instructions)
	{
		_instructions = instructions;
	}

	public int getMaxStack()
	{
		return _maxStack;
	}

	public void setMaxStack(int maxStack)
	{
		_maxStack = maxStack;
	}

	public int getMaxLocals()
	{
		return _maxLocals;
	}

	public void setMaxLocals(int maxLocals)
	{
		_maxLocals = maxLocals;
	}

	public void setLocalVariables(LocalVariable[] localVariables)
	{
		_localVariables = localVariables;
	}
}
