package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ArrayObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class areturn extends Instruction
{
	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{

	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		ObjectInfo last = context.last();

		AssertUtil.assertFalse(last instanceof ArrayObjectInfo || last == VmUtil.OBJECT_NULL);

		context.push(last);
	}
}