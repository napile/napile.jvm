package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.bytecode.impl.IsInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @since 19:13/04.10.12
 */
public class VmIsInstruction extends VmInstruction<IsInstruction>
{
	public VmIsInstruction(@NotNull IsInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		BaseObjectInfo objectInfo = context.pop();

		context.push(VmUtil.convertToVm(vm, context, vm.isEqualOrSubType(context, instruction.value, objectInfo.getTypeNode())));

		return nextIndex;
	}
}
