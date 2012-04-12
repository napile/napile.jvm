package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.cached.MethodRefConstant;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class invokespecial extends Instruction
{
	private static final Logger LOGGER = Logger.getLogger(invokespecial.class);

	private int _index;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		_index = buffer.getShort();
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		StackEntry entry = context.getLastStack();

		ObjectInfo objectInfo = context.last();

		if(objectInfo == VmUtil.OBJECT_NULL)
			objectInfo = null;

		MethodRefConstant constant = (MethodRefConstant)entry.getConstantPool().getConstant(_index);

		MethodInfo methodInfo = AssertUtil.assertNull(constant.getMethodInfo(vm));
		List<ObjectInfo> arguments = new ArrayList<ObjectInfo>(methodInfo.getParameters().length);
		for(int i = 0; i < methodInfo.getParameters().length; i++)
			arguments.add(context.last());

		Collections.reverse(arguments);

		StackEntry nextEntry = new StackEntry(objectInfo, methodInfo, arguments.toArray(new ObjectInfo[arguments.size()]));

		context.getStack().add(nextEntry);

		vm.invoke(methodInfo, objectInfo, context, ObjectInfo.EMPTY_ARRAY);

		context.getStack().pollLast();

		//if(LOGGER.isDebugEnabled())
		//	LOGGER.info("invokespecial: " + methodInfo.toString());
	}
}
