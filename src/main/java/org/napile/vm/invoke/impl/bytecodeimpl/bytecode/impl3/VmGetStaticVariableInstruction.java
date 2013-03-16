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

import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.bytecode.impl.GetStaticVariableInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @since 19:59/21.09.12
 */
public class VmGetStaticVariableInstruction extends VmInstruction<GetStaticVariableInstruction>
{
	private FqName className;
	private String name;

	public VmGetStaticVariableInstruction(GetStaticVariableInstruction instruction)
	{
		super(instruction);

		className = instruction.variableRef.variable.parent();
		name = instruction.variableRef.variable.shortName().getName();
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		ClassInfo classInfo = vm.getClass(className);

		AssertUtil.assertNull(classInfo);

		VariableInfo variableInfo = vm.getStaticField(classInfo, name, true);

		AssertUtil.assertNull(variableInfo);

		context.push(variableInfo.getStaticValue());

		return nextIndex;
	}
}
