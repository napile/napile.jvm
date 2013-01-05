package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.bytecode.impl.SwapInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 12:28/29.11.12
 */
public class VmSwapInstruction extends VmInstruction<SwapInstruction>
{
	public VmSwapInstruction(@NotNull SwapInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		BaseObjectInfo o1 = context.pop();
		BaseObjectInfo o2 = context.pop();
		context.push(o1);
		context.push(o2);
		return nextIndex;
	}
}
