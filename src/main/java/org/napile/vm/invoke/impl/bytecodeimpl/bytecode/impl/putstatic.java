package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.cached.FieldWrapConstant;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class putstatic extends Instruction
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
		StackEntry stackEntry = context.getLastStack();

		FieldWrapConstant wrapConstant = (FieldWrapConstant) stackEntry.getConstantPool().getConstant(_index);

		FieldInfo fieldInfo = wrapConstant.getFieldInfo(vm);
		if(!Flags.isStatic(fieldInfo))
			return;

		ObjectInfo value = context.last();

		AssertUtil.assertString(VmUtil.canSetValue(fieldInfo.getType(), value.getClassInfo()));

		fieldInfo.setValue(value);
	}

	@Override
	public String toString()
	{
		return super.toString() + ": " + _index;
	}
}
