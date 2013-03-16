package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.napile.asm.tree.members.bytecode.impl.InvokeAnonymInstruction;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.CallPosition;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 19:16/29.11.12
 */
public class VmInvokeAnonymInstruction extends VmInstruction<InvokeAnonymInstruction>
{
	private TypeNode[] parameterTypes;

	public VmInvokeAnonymInstruction(InvokeAnonymInstruction instruction)
	{
		super(instruction);

		parameterTypes = new TypeNode[instruction.methodRef.parameters.size()];
		for(int i = 0; i < parameterTypes.length; i++)
			parameterTypes[i] = instruction.methodRef.parameters.get(i).returnType;
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		BaseObjectInfo[] arguments = new BaseObjectInfo[parameterTypes.length];
		for(int i = 0; i < parameterTypes.length; i++)
			arguments[i] = context.pop();

		arguments = ArrayUtil.reverseArray(arguments);

		BaseObjectInfo link = context.pop();

		VmPutAnonymInstruction.AnonymContext data = link.value();

		List<BaseObjectInfo> args = new ArrayList<BaseObjectInfo>(arguments.length);
		Collections.addAll(args, arguments);

		StackEntry nextEntry = data.stackEntry;
		nextEntry.position = new CallPosition(this);
		for(int i = 0; i < arguments.length; i++)
			nextEntry.setValue(i, arguments[i]);

		context.getStack().add(nextEntry);

		vm.invoke(context, data.invokeType);

		StackEntry stackEntry = context.getStack().pollLast();
		if(stackEntry == null)
			return BREAK_INDEX;

		for(BaseObjectInfo returnValue : stackEntry.getReturnValues())
			context.push(returnValue);

		int forceIndex = nextEntry.getForceIndex();
		return forceIndex == -2 ? nextIndex : forceIndex;
	}
}
