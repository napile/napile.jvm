package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.AsmConstants;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.bytecode.impl.LinkStaticMethodInstruction;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 18:22/14.01.13
 */
public class VmLinkStaticMethodInstruction extends VmInstruction<LinkStaticMethodInstruction>
{
	private FqName className;
	private String methodName;
	private TypeNode[] parameters;

	public VmLinkStaticMethodInstruction(@NotNull LinkStaticMethodInstruction instruction)
	{
		super(instruction);
		className = instruction.methodRef.method.parent();
		methodName = instruction.methodRef.method.shortName().getName();
		parameters = instruction.methodRef.parameters.toArray(new TypeNode[instruction.methodRef.parameters.size()]);

	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		ClassInfo classInfo = vm.safeGetClass(className);

		MethodInfo methodInfo = AssertUtil.assertNull(vm.getStaticMethod(classInfo, methodName, false, parameters));

		BaseObjectInfo link = new BaseObjectInfo(vm, vm.safeGetClass(NapileLangPackage.ANY), AsmConstants.ANY_TYPE);
		link.value(new Object[]{null, methodInfo});
		context.push(link);
		return nextIndex;
	}
}
