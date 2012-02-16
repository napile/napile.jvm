package org.napile.vm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.interpreter.StackEntry;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.cached.FieldWrapConstant;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ClassObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.VmInterface;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class putfield implements Instruction
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
		StackEntry stackEntry = context.getLastStack();

		FieldWrapConstant fieldWrapConstant = (FieldWrapConstant) stackEntry.getConstantPool().getConstant(_index);

		FieldInfo fieldInfo = fieldWrapConstant.getFieldInfo(vmInterface);

		ObjectInfo value = context.last();
		ClassObjectInfo object = (ClassObjectInfo)context.last();

		AssertUtil.assertFalse(object.getFields().containsKey(fieldInfo));
		AssertUtil.assertFalse(VmUtil.canSetValue(fieldInfo.getType(), value.getClassInfo()));

		object.getFields().put(fieldInfo, value);
	}
}
