package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.bytecode.impl.ThrowInstruction;
import org.napile.asm.tree.members.bytecode.tryCatch.CatchBlock;
import org.napile.asm.tree.members.bytecode.tryCatch.TryBlock;
import org.napile.asm.tree.members.bytecode.tryCatch.TryCatchBlockNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @since 14:59/05.10.12
 */
public class VmThrowInstruction extends VmInstruction<ThrowInstruction>
{
	public VmThrowInstruction(@NotNull ThrowInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		final BaseObjectInfo object = context.pop();

		final StackEntry last = context.getLastStack();
		//final Deque<StackEntry> copy = new ArrayDeque<StackEntry>(context.getStack());

		// reverse iterator, first element is this stackentry
		Iterator<StackEntry> iterator = context.getStack().descendingIterator();
		while(iterator.hasNext())
		{
			StackEntry stackEntry = iterator.next();

			Collection<TryCatchBlockNode> tryCatchBlockNodes = stackEntry.getTryCatchBlockNodes();
			for(TryCatchBlockNode tryCatchBlockNode : tryCatchBlockNodes)
			{
				TryBlock tryBlock = tryCatchBlockNode.tryBlock;

				if(tryBlock.startIndex <= nextIndex && tryBlock.endIndex >= nextIndex)
				{
					for(CatchBlock catchBlock : tryCatchBlockNode.catchBlocks)
					{
						if(vm.isEqualOrSubType(context, object.getTypeNode(), catchBlock.exception))
						{
							stackEntry.setValue(catchBlock.variableIndex, object);

							if(stackEntry == last)
								return catchBlock.startIndex;
							else
							{
								stackEntry.setForceIndex(catchBlock.startIndex);

								last.setForceIndex(BREAK_INDEX);
								return BREAK_INDEX;
							}
						}
					}
				}
			}

			iterator.remove();
		}

		MethodInfo methodInfo = vm.getMethod(object.getClassInfo(), "printStackTrace", true, new TypeNode[0]);

		AssertUtil.assertFalse(methodInfo != null, "`printStackTrace` is not found");

		vm.invoke(new InterpreterContext(new StackEntry(object, methodInfo, BaseObjectInfo.EMPTY_ARRAY, Collections.<TypeNode>emptyList())), methodInfo.getInvokeType());

		return -1;
	}
}
