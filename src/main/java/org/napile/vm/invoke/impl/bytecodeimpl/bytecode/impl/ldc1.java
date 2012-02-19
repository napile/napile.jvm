package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.ShortValueConstant;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.Utf8ValueConstant;
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

		ShortValueConstant constant = (ShortValueConstant)constantPool.getConstant(_index);

		Utf8ValueConstant utf8ValueConstant = (Utf8ValueConstant)constantPool.getConstant(constant.getValue());

		ClassInfo javaLangString = vm.getClass(Vm.JAVA_LANG_STRING);

		ObjectInfo objectInfo = VmUtil.convertToVm(vm, javaLangString, utf8ValueConstant.getValue());

		context.push(objectInfo);
	}
}
