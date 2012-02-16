package org.napile.vm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.interpreter.StackEntry;
import org.napile.vm.objects.objectinfo.impl.primitive.IntObjectInfo;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class if_icmpgt implements Instruction
{
	private int _index;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		_index = buffer.getShort();
	}

	@Override
	public void call(VmInterface vmInterface, InterpreterContext context)
	{
		StackEntry entry = context.getLastStack();

		IntObjectInfo objectInfo1 = (IntObjectInfo)context.last();
		IntObjectInfo objectInfo2 = (IntObjectInfo)context.last();
		System.out.println(_index);
	}
}
