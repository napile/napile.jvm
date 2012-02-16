package org.napile.vm.invoke.impl.bytecodeimpl.bytecode;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 10:16/05.02.2012
 */
public interface Instruction
{
	Instruction[] EMPTY_ARRAY = new Instruction[0];

	void parseData(ByteBuffer buffer, boolean wide);

	void call(VmInterface vmInterface, InterpreterContext context);
}
