package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.classinfo.parsing.constantpool.ValueConstant;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.ShortValueConstant;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class ldc1 extends Instruction
{
	private int _index;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		_index = buffer.get();
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		ConstantPool constantPool = context.getLastStack().getConstantPool();

		ValueConstant<?> valueConstant =  (ValueConstant) constantPool.getConstant(_index);
		if(valueConstant.getType() == ConstantPool.CP_STRING)
			valueConstant = (ValueConstant)constantPool.getConstant(((ShortValueConstant)valueConstant).getValue());

		ObjectInfo objectInfo = VmUtil.convertToVm(vm, valueConstant.getValue());

		context.push(objectInfo);
	}

	@Override
	public String toString()
	{
		return super.toString() + ": " + _index;
	}
}
