package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.napile.asm.tree.members.bytecode.impl.InvokeAnonymInstruction;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.vm.Vm;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @date 19:16/29.11.12
 */
public class VmInvokeAnonymInstruction extends VmInstruction<InvokeAnonymInstruction>
{
	private TypeNode[] parameters;

	public VmInvokeAnonymInstruction(InvokeAnonymInstruction instruction)
	{
		super(instruction);

		parameters = instruction.methodRef.parameters.toArray(new TypeNode[instruction.methodRef.parameters.size()]);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		BaseObjectInfo[] arguments = new BaseObjectInfo[parameters.length];
		for(int i = 0; i < parameters.length; i++)
			arguments[i] = context.pop();

		arguments = ArrayUtil.reverseArray(arguments);

		BaseObjectInfo link = context.pop();

		Object[] data = (Object[]) link.value();

		StackEntry nextEntry = new StackEntry((BaseObjectInfo) data[0], (MethodInfo) data[1], arguments, instruction.methodRef.typeArguments);

		context.getStack().add(nextEntry);

		vm.invoke(context);

		StackEntry stackEntry = context.getStack().pollLast();
		if(stackEntry == null)
			return BREAK_INDEX;

		context.push(stackEntry.getReturnValue(false));

		int forceIndex = stackEntry.getForceIndex();
		return forceIndex == -2 ? nextIndex : forceIndex;
	}
}
