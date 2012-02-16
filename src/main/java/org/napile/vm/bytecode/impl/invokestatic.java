package org.napile.vm.bytecode.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.interpreter.Interpreter;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.interpreter.StackEntry;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.cached.MethodRefConstant;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class invokestatic implements Instruction
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
		StackEntry entry = context.getLastStack();

		MethodRefConstant constant = (MethodRefConstant)entry.getConstantPool().getConstant(_index);

		MethodInfo methodInfo = AssertUtil.assertNull(constant.getMethodInfo(vmInterface));
		List<ObjectInfo> arguments = new ArrayList<ObjectInfo>(methodInfo.getParameters().length);
		for(int i = 0; i < methodInfo.getParameters().length; i++)
			arguments.add(context.last());

		Collections.reverse(arguments);

		StackEntry nextEntry = new StackEntry(null, methodInfo, arguments.toArray(new ObjectInfo[arguments.size()]));

		context.getStack().add(nextEntry);

		Interpreter interpreter = new Interpreter(methodInfo.getInstructions(), vmInterface);
		interpreter.call(context);
	}
}
