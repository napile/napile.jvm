package org.napile.vm.invoke.impl.nativeimpl.classes;

import java.util.Collections;

import org.napile.asm.AsmConstants;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.vm.invoke.impl.NativeThrowException;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 20:10/14.01.13
 */
public class napile_reflect_Method
{
	public static final TypeNode NAPILE_LANG_ARRAY__ANY_NULLABLE__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(new TypeNode(true, new ClassTypeNode(NapileLangPackage.ANY)));

	@NativeImplement(methodName = "invokeStatic0", parameters = {"napile.lang.Array<napile.lang.Any?>"})
	public static BaseObjectInfo invokeStatic0(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		MethodInfo methodInfo = objectInfo.value();
		BaseObjectInfo[] arguments = context.getLastStack().getArguments();

		StackEntry stackEntry = new StackEntry(null, methodInfo, arguments, Collections.<TypeNode>emptyList());

		context.getStack().add(stackEntry);

		vm.invoke(context, methodInfo.getInvokeType());

		context.getStack().remove(stackEntry);

		if(stackEntry.getForceIndex() != -2)
			throw new NativeThrowException(stackEntry.getForceIndex());

		BaseObjectInfo[] returnValues = stackEntry.getReturnValues(false);
		BaseObjectInfo array = vm.newObject(context, NAPILE_LANG_ARRAY__ANY_NULLABLE__, new TypeNode[]{AsmConstants.INT_TYPE}, new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, returnValues.length)});
		BaseObjectInfo[] objectsOfArray = array.value();
		System.arraycopy(returnValues, 0, objectsOfArray, 0, objectsOfArray.length);
		return array;
	}
}
