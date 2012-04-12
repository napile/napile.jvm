package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.cached.MethodRefConstant;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ClassObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class invokevirtual extends Instruction
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
		StackEntry entry = context.getLastStack();

		MethodRefConstant constant = (MethodRefConstant)entry.getConstantPool().getConstant(_index);

		MethodInfo methodInfo = AssertUtil.assertNull(constant.getMethodInfo(vm));
		ClassInfo classInfo = methodInfo.getParent();
		ClassInfo targetClassInfo = entry.getMethodInfo().getParent();
		if(classInfo.getName().equals(Vm.JAVA_LANG_CLASS))
		{
			List<ObjectInfo> arguments = new ArrayList<ObjectInfo>(methodInfo.getParameters().length);
			for(int i = 0; i < methodInfo.getParameters().length; i++)
				arguments.add(context.last());

			Collections.reverse(arguments);

			ClassObjectInfo classObjectInfo = vm.getClassObjectInfo(targetClassInfo);

			StackEntry nextEntry = new StackEntry(classObjectInfo, methodInfo, arguments.toArray(new ObjectInfo[arguments.size()]));

			context.getStack().add(nextEntry);

			vm.invoke(methodInfo, classObjectInfo, context, ObjectInfo.EMPTY_ARRAY);

			context.getStack().pollLast();
		}
		else
			throw new IllegalArgumentException();
	}

	@Override
	public String toString()
	{
		return super.toString() + ": " + _index;
	}
}
