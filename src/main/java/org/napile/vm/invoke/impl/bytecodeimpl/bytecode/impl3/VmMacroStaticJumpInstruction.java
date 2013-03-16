package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.bytecode.impl.MacroStaticJumpInstruction;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.CallPosition;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 18:03/21.11.12
 */
public class VmMacroStaticJumpInstruction extends VmInstruction<MacroStaticJumpInstruction>
{
	private FqName className;
	private String methodName;
	private TypeNode[] parameters;

	public VmMacroStaticJumpInstruction(MacroStaticJumpInstruction instruction)
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

		MethodInfo methodInfo = vm.getStaticMacro(classInfo, methodName, false, parameters);

		AssertUtil.assertFalse(methodInfo != null, "Macro not found " + methodName + " " + className + " parameters " + StringUtil.join(instruction.methodRef.parameters, ", "));

		BaseObjectInfo[] arguments = new BaseObjectInfo[methodInfo.getParameters().length];
		for(int i = 0; i < methodInfo.getParameters().length; i++)
			arguments[i] = context.pop();

		arguments = ArrayUtil.reverseArray(arguments);

		StackEntry nextEntry = new StackEntry(null, methodInfo, arguments, instruction.methodRef.typeArguments);
		nextEntry.position = new CallPosition(this);

		context.getStack().add(nextEntry);

		vm.invoke(context, methodInfo.getInvokeType());

		StackEntry stackEntry = context.getStack().pollLast();
		if(stackEntry == null)
			return -1;

		StackEntry prevEntry = context.getLastStack();

		BaseObjectInfo[] returnValues = stackEntry.getReturnValues();
		if(returnValues != null)
		{
			prevEntry.initReturnValues(returnValues.length);
			for(int i = 0; i < returnValues.length; i++)
				prevEntry.setReturnValue(i, returnValues[i]);
			prevEntry.setForceIndex(BREAK_INDEX);
			return BREAK_INDEX;
		}

		int forceIndex = stackEntry.getForceIndex();
		return forceIndex == -2 ? nextIndex : forceIndex;
	}
}
