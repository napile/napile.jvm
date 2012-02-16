package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class invokespecial implements Instruction
{
	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		int val = buffer.getShort();
	}

	@Override
	public void call(VmInterface vmInterface, InterpreterContext context)
	{
		StackEntry stackEntry = context.getLastStack();

		context.last();
	}
}
