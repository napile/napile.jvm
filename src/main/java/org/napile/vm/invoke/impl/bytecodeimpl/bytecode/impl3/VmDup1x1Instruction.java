package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.bytecode.impl.Dup1x1Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 20:23/06.10.12
 */
public class VmDup1x1Instruction extends VmInstruction<Dup1x1Instruction>
{
	public VmDup1x1Instruction(@NotNull Dup1x1Instruction instruction)
	{
		super(instruction);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		BaseObjectInfo target = context.pop();
		BaseObjectInfo center = context.pop();

		context.push(target);
		context.push(center);
		context.push(target);
		return nextIndex;
	}
}
