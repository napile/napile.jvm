package org.napile.vm.interpreter;

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
				debug(i, e);

				System.exit(-1);
			}
		}

		for(WorkData workData : context.getStack())
			LOGGER.info(workData.getMethodInfo().toString());
		debug(-1, null);
		LOGGER.info("-----------------------------------");
	}

	private void debug(int errorIndex, Exception e)
	{
		for(int j = 0; j < _instructions.length; j++)
		LOGGER.info(j + " [" + _instructions[j].getClass().getSimpleName() + "]" + ((errorIndex == j) ? " ERROR: " + e.getMessage() : ""));
		if(e != null)
			e.printStackTrace();
	}
}
