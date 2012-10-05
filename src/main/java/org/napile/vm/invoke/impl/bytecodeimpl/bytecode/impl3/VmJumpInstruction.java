package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.napile.asm.tree.members.bytecode.impl.JumpInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 18:23/27.09.12
 */
public class VmJumpInstruction extends VmInstruction<JumpInstruction>
{
	public VmJumpInstruction(JumpInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		return instruction.value;
	}
}
