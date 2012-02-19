package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.cached.MethodRefConstant;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class invokevirtual extends Instruction
{
	private int _index;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		_index = buffer.getShort();
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		StackEntry entry = context.getLastStack();

		MethodRefConstant constant = (MethodRefConstant)entry.getConstantPool().getConstant(_index);

		MethodInfo methodInfo = AssertUtil.assertNull(constant.getMethodInfo(vm));

		throw new IllegalArgumentException();
	}

	@Override
	public String toString()
	{
		return super.toString() + ": " + _index;
	}
}
