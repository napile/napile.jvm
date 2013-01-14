package org.napile.vm.invoke.impl.nativeimpl.classes;

import java.util.Collections;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 20:10/14.01.13
 */
public class napile_reflect_Method
{
	@NativeImplement(className = "napile.reflect.Method", methodName = "invokeStatic0", parameters = {"napile.lang.Array<napile.lang.Any?>"})
	public static BaseObjectInfo invokeStatic0(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		MethodInfo methodInfo = objectInfo.value();
		BaseObjectInfo[] arguments = context.getLastStack().getArguments();

		StackEntry stackEntry = new StackEntry(null, methodInfo, arguments, Collections.<TypeNode>emptyList());

		context.getStack().add(stackEntry);

		vm.invoke(context);

		context.getStack().remove(stackEntry);

		return stackEntry.getReturnValue(false);
	}
}
