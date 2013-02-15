/*
 * Copyright 2010-2012 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl3;

import org.napile.asm.tree.members.bytecode.impl.NewObjectInstruction;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.asm.tree.members.types.constructors.TypeParameterValueTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @date 19:58/21.09.12
 */
public class VmNewObjectInstruction extends VmInstruction<NewObjectInstruction>
{
	public VmNewObjectInstruction(NewObjectInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		TypeNode createType = null;
		if(instruction.value.typeConstructorNode instanceof TypeParameterValueTypeNode)
		{
			TypeNode typeNode = context.searchTypeParameterValue(((TypeParameterValueTypeNode) instruction.value.typeConstructorNode).name);

			AssertUtil.assertFalse(typeNode != null, "Type parameter is not found : " + ((TypeParameterValueTypeNode) instruction.value.typeConstructorNode).name);

			createType = typeNode;
		}
		else if(instruction.value.typeConstructorNode instanceof ClassTypeNode)
			createType = instruction.value;
		else
			throw new UnsupportedOperationException(instruction.value.typeConstructorNode + " cant be created by " + instruction);

		assert createType != null;

		BaseObjectInfo[] arguments = new BaseObjectInfo[instruction.parameters.size()];
		for(int i = 0; i < arguments.length; i++)
			arguments[i] = context.pop();

		arguments = ArrayUtil.reverseArray(arguments);

		context.push(vm.newObject(context, this, createType, instruction.parameters.toArray(new TypeNode[instruction.parameters.size()]), arguments));
		return nextIndex;
	}
}
