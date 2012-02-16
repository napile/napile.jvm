package org.napile.vm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.interpreter.WorkData;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.cached.FieldWrapConstant;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.VmInterface;
import org.napile.vm.vm.VmUtil;

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

		FieldWrapConstant wrapConstant = (FieldWrapConstant)workData.getConstantPool().getConstant(_index);

		FieldInfo fieldInfo = wrapConstant.getFieldInfo();
		if(!Flags.isStatic(fieldInfo))
			return;

		ObjectInfo value = context.pop();

		AssertUtil.assertFalse(VmUtil.canSetValue(fieldInfo.getType(), value.getClassInfo()));

		fieldInfo.setValue(value);
	}
}
