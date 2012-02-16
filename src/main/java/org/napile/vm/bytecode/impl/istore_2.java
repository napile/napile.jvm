package org.napile.vm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.interpreter.WorkData;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.VmInterface;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class istore_2 implements Instruction
{
	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{

	}

	@Override
	public void call(VmInterface vmInterface, InterpreterContext context)
	{
		WorkData workData = context.getLastWork();

		ObjectInfo value = context.pop();

		if(workData.getLocalVariables().size() == 2) // no local variable at index 2
			workData.getLocalVariables().add(value);
		else
		{
			AssertUtil.assertFalse(VmUtil.canSetValue(workData.getParentVariables()[2].getType(), value.getClassInfo()));

			workData.getLocalVariables().set(2, value);
		}
	}
}
