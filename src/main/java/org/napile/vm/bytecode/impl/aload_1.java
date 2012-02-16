package org.napile.vm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.interpreter.WorkData;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.util.CollectionUtil;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class aload_1 implements Instruction
{
	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{

	}

	@Override
	public void call(VmInterface vmInterface, InterpreterContext context)
	{
		WorkData workData = context.getLastWork();

		AssertUtil.assertFalse(workData.getArguments().length > 0);

		ObjectInfo value = workData.getArguments()[0];

		ObjectInfo objectInfo = CollectionUtil.safeGet(workData.getLocalVariables(), 1);
		if(objectInfo == null)
			workData.getLocalVariables().add(objectInfo = value);

		context.push(objectInfo);
	}
}
