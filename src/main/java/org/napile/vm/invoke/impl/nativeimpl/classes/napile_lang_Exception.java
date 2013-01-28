package org.napile.vm.invoke.impl.nativeimpl.classes;

import java.util.Deque;
import java.util.Iterator;

import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 16:08/05.10.12
 */
public class napile_lang_Exception
{
	public static final TypeNode STACK_TRACE_ELEMENT = new TypeNode(false, new ClassTypeNode(new FqName("napile.lang.StackTraceElement")));

	public static final TypeNode ARRAY__STACK_TRACE_ELEMENT__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(STACK_TRACE_ELEMENT);

	@NativeImplement(methodName = "generateStack", parameters = {})
	public static BaseObjectInfo generateStack(Vm vm, InterpreterContext context)
	{
		Deque<StackEntry> list = context.getStack();

		BaseObjectInfo arrayObject = vm.newObject(context, ARRAY__STACK_TRACE_ELEMENT__, VmUtil.varargTypes(VmUtil.INT), new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, list.size())});

		BaseObjectInfo[] array = arrayObject.value();
		int i = 0;

		Iterator<StackEntry> iterator = context.getStack().descendingIterator();
		while(iterator.hasNext())
		{
			StackEntry stackEntry = iterator.next();

			array[i++] = vm.newObject(context, STACK_TRACE_ELEMENT, VmUtil.varargTypes(VmUtil.STRING), new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, stackEntry.getMethodInfo() == null ? "anonym" : stackEntry.getMethodInfo().getFqName().getFqName())});
		}
		return arrayObject;
	}
}
