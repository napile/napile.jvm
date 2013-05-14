package org.napile.vm.invoke.impl.nativeimpl.classes;

import java.util.Deque;
import java.util.Iterator;

import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.CallPosition;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @since 16:08/05.10.12
 */
public class napile_lang_Exception
{
	public static final TypeNode STACK_TRACE_ELEMENT = new TypeNode(false, new ClassTypeNode(new FqName("napile.lang.StackTraceElement")));

	public static final TypeNode ARRAY__STACK_TRACE_ELEMENT__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(STACK_TRACE_ELEMENT);

	public static final TypeNode[] ARGUMENTS = VmUtil.varargTypes(VmUtil.STRING, VmUtil.STRING, VmUtil.STRING, VmUtil.INT, VmUtil.INT);

	@NativeImplement(methodName = "generateStack", parameters = {})
	public static BaseObjectInfo generateStack(Vm vm, InterpreterContext context)
	{
		Deque<StackEntry> list = context.getStack();

		BaseObjectInfo arrayObject = VmUtil.createArray(vm, context, ARRAY__STACK_TRACE_ELEMENT__, list.size() - 1);

		BaseObjectInfo[] array = arrayObject.value();
		int i = 0;

		Iterator<StackEntry> iterator = context.getStack().descendingIterator();
		iterator.next(); // skip first

		while(iterator.hasNext())
		{
			StackEntry stackEntry = iterator.next();

			CallPosition callPosition = stackEntry.position;

			array[i++] = vm.newObject(context, null, STACK_TRACE_ELEMENT, ARGUMENTS, new BaseObjectInfo[]
			{
					VmUtil.convertToVm(vm, context, callPosition.className),
					VmUtil.convertToVm(vm, context, callPosition.methodName),
					VmUtil.convertToVm(vm, context, callPosition.fileName),
					VmUtil.convertToVm(vm, context, callPosition.line),
					VmUtil.convertToVm(vm, context, callPosition.column)
			});
		}
		return arrayObject;
	}
}
