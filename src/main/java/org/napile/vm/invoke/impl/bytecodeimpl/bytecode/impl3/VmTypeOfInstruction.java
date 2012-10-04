package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.napile.asm.tree.members.bytecode.impl.TypeOfInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 12:35/04.10.12
 */
public class VmTypeOfInstruction extends VmInstruction<TypeOfInstruction>
{
	public VmTypeOfInstruction(TypeOfInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		context.push(vm.createTypeObject(instruction.value));
	}
}
