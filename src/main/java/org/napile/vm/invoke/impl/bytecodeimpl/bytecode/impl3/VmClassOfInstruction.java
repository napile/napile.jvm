package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.bytecode.impl.ClassOfInstruction;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.asm.tree.members.types.constructors.TypeParameterValueTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 12:00/04.10.12
 */
public class VmClassOfInstruction extends VmInstruction<ClassOfInstruction>
{
	public VmClassOfInstruction(ClassOfInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		ClassInfo classInfo = vm.getClass(getFqName(instruction.value, context));

		AssertUtil.assertFalse(classInfo != null, "Class not found");

		context.push(vm.getOrCreateClassObject(context, classInfo));

		return nextIndex;
	}

	private static FqName getFqName(@NotNull TypeNode typeNode, @NotNull InterpreterContext context)
	{
		if(typeNode.typeConstructorNode instanceof ClassTypeNode)
			return ((ClassTypeNode) typeNode.typeConstructorNode).className;
		else if(typeNode.typeConstructorNode instanceof TypeParameterValueTypeNode)
		{
			TypeNode temp = context.searchTypeParameterValue(((TypeParameterValueTypeNode) typeNode.typeConstructorNode).name);

			return getFqName(temp, context);
		}
		else
			throw new UnsupportedOperationException("Unknown how get class info : " + typeNode.typeConstructorNode);
	}
}
