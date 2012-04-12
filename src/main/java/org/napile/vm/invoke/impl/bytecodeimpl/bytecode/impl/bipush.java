package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.objectinfo.impl.primitive.ByteObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class bipush extends Instruction
{
	private byte  _index;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		_index = buffer.get();
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		context.push(new ByteObjectInfo(vm.getClass(Vm.PRIMITIVE_BYTE), _index));
	}
}
