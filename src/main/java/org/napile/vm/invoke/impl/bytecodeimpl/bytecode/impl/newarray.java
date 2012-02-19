package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ArrayObjectInfo;
import org.napile.vm.objects.objectinfo.impl.primitive.IntObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class newarray extends Instruction
{
	/**
	 boolean
	 4
	 char
	 5
	 float
	 6
	 double
	 7
	 byte
	 8
	 short
	 9
	 int
	 10
	 long
	 11
	 */
	private int _index;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		_index = buffer.get();
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		StackEntry stackEntry = context.getLastStack();

		IntObjectInfo objectInfo = (IntObjectInfo)context.last();

		int size = objectInfo.getValue();
		ObjectInfo[] array = new ObjectInfo[size];

		ClassInfo classInfo = null;
		switch(_index)
		{
			case 4:
				classInfo = vm.getClass(Vm.PRIMITIVE_BOOLEAN_ARRAY);
				break;
			case 5:
				classInfo = vm.getClass(Vm.PRIMITIVE_CHAR_ARRAY);
				break;
			default:
				AssertUtil.assertTrue(true);
		}

		ArrayObjectInfo arrayObjectInfo = new ArrayObjectInfo(null, classInfo, array);

		context.push(arrayObjectInfo);
	}
}
