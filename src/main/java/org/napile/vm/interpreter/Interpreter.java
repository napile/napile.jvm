package org.napile.vm.interpreter;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:21/06.02.2012
 */
public class Interpreter
{
	private Instruction[] _instructions;
	private VmInterface _vmInterface;

	public Interpreter(Instruction[] instructions, VmInterface vmInterface)
	{
		_instructions = instructions;
		_vmInterface = vmInterface;
	}

	public void call(InterpreterContext context)
	{
		for(Instruction instruction : _instructions)
			instruction.call(_vmInterface, context);
	}
}
