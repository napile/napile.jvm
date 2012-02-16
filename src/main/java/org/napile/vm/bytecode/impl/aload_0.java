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
public class aload_0 implements Instruction
{
	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{

	}

	@Override
	public void call(VmInterface vmInterface, InterpreterContext context)
	{
		WorkData workData = context.getLastWork();

		if(workData.getObjectInfo() != null)
		{
			ObjectInfo objectInfo = workData.getObjectInfo();

			ObjectInfo existsObject = CollectionUtil.safeGet(workData.getLocalVariables(), 0);
			if(existsObject == null)
				workData.getLocalVariables().add(existsObject = objectInfo);

			context.push(existsObject);
		}
		else
			AssertUtil.assertTrue(true);
	}
}
