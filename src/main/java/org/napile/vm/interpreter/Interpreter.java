package org.napile.vm.interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.napile.vm.bytecode.Instruction;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:21/06.02.2012
 */
public class Interpreter
{
	private static final Logger LOGGER = Logger.getLogger(Interpreter.class);
	private Instruction[] _instructions;
	private VmInterface _vmInterface;

	public Interpreter(Instruction[] instructions, VmInterface vmInterface)
	{
		_instructions = instructions;
		_vmInterface = vmInterface;
	}

	public void call(InterpreterContext context)
	{
		for(int i = 0; i < _instructions.length; i++)
		{
			Instruction instruction = _instructions[i];

			try
			{
				instruction.call(_vmInterface, context);
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
}
