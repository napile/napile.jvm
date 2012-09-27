package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.napile.asm.tree.members.bytecode.impl.JumpIfInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 18:22/27.09.12
 */
public class VmJumpIfInstruction extends VmInstruction<JumpIfInstruction>
{
	private boolean isFailed;

	public VmJumpIfInstruction(JumpIfInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public int getNextIndex(int index)
	{
		return isFailed ? instruction.value : super.getNextIndex(index);
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo val = context.pop();
		BaseObjectInfo condVal = context.pop();

		isFailed = val != condVal;
	}
}
