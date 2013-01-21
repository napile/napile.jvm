package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.napile.asm.tree.members.bytecode.impl.InvokeAnonymInstruction;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
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

		VmPutAnonymInstruction.AnonymContext data = link.value();

		List<BaseObjectInfo> args = new ArrayList<BaseObjectInfo>(arguments.length + data.require.length);
		Collections.addAll(args, arguments);
		Collections.addAll(args, data.require);

		StackEntry nextEntry = new StackEntry(data.invokeType.getMaxLocals(), args.toArray(new BaseObjectInfo[args.size()]), data.tryCatchBlockNodes);

		context.getStack().add(nextEntry);

		vm.invoke(context, data.invokeType);

		StackEntry stackEntry = context.getStack().pollLast();
		if(stackEntry == null)
			return BREAK_INDEX;

		context.push(nextEntry.getReturnValue(false));

		int forceIndex = nextEntry.getForceIndex();
		return forceIndex == -2 ? nextIndex : forceIndex;
	}
}
