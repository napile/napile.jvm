package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class if_acmpne implements Instruction
{
	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		buffer.getShort();
	}

	@Override
	public void call(VmInterface vmInterface, InterpreterContext context)
	{

	}
}
