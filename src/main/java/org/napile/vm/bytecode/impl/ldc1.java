package org.napile.vm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.classinfo.parsing.constantpool.value.ShortValueConstant;
import org.napile.vm.objects.classinfo.parsing.constantpool.value.Utf8ValueConstant;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class ldc1 implements Instruction
{
	private int _index;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		_index = buffer.get();
	}

	@Override
	public void call(VmInterface vmInterface, InterpreterContext context)
	{
		ClassInfo classInfo = context.getLastMethod().getParent();

		ConstantPool constantPool = classInfo.getConstantPool();

		ShortValueConstant constant = (ShortValueConstant)constantPool.getConstant(_index);

		Utf8ValueConstant utf8ValueConstant = (Utf8ValueConstant)constantPool.getConstant(constant.getValue());

		System.out.println(utf8ValueConstant.getValue());
	}
}
