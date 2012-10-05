package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.napile.asm.tree.members.bytecode.impl.JumpIfInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 18:22/27.09.12
 */
public class VmJumpIfInstruction extends VmInstruction<JumpIfInstruction>
{
	public VmJumpIfInstruction(JumpIfInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		BaseObjectInfo val = context.pop();
		BaseObjectInfo condVal = context.pop();

		if(!val.getTypeNode().equals(VmUtil.BOOL) || !condVal.getTypeNode().equals(VmUtil.BOOL))
			throw new IllegalArgumentException();

		return val != condVal ? instruction.value : nextIndex;
	}
}
