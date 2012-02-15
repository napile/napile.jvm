package org.napile.vm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.interpreter.WorkData;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.cached.FieldConstant;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class putstatic implements Instruction
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
		WorkData workData = context.getLastWork();

		FieldConstant constant = (FieldConstant)workData.getConstantPool().getConstant(_index);

		FieldInfo fieldInfo = vmInterface.getStaticField(constant.getClassInfo(), constant.getName());

		fieldInfo.setValue(context.pop());
	}
}
