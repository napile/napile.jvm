package org.napile.vm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.objects.objectinfo.impl.primitive.IntObjectInfo;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class iconst_0 implements Instruction
{
	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		//
	}

	@Override
	public void call(VmInterface vmInterface, InterpreterContext context)
	{
		context.push(new IntObjectInfo(null, vmInterface.getClass(VmInterface.PRIMITIVE_INT), 0));
	}
}
