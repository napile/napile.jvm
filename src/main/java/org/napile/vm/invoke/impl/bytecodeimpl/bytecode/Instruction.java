package org.napile.vm.invoke.impl.bytecodeimpl.bytecode;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 10:16/05.02.2012
 */
public abstract class Instruction
{
	public static final Instruction[] EMPTY_ARRAY = new Instruction[0];

	public abstract void parseData(ByteBuffer buffer, boolean wide);

	public abstract void call(Vm vm, InterpreterContext context);

	public int getNextIndex(int index)
	{
		return index + 1;
	}
}
