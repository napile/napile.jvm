/*
 * Copyright 2010-2013 napile.org
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

package org.napile.vm.jit.impl.instruction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.tree.members.bytecode.impl.NewObjectInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @since 13:29/03.02.13
 */
public class JitNewArrayInstruction extends VmInstruction<NewObjectInstruction>
{
	private final Object arrayValue;

	public JitNewArrayInstruction(@NotNull NewObjectInstruction instruction, @Nullable Object arrayValue)
	{
		super(instruction);
		this.arrayValue = arrayValue;
	}

	@Override
	public int call(Vm vm, InterpreterContext context, int nextIndex)
	{
		BaseObjectInfo sizeObject = context.pop();
		Integer size = VmUtil.convertToJava(vm, sizeObject);

		ClassInfo classInfo = vm.safeGetClass(NapileLangPackage.ARRAY);

		BaseObjectInfo arrayObjectValue = VmUtil.convertToVm(vm, context, arrayValue);

		BaseObjectInfo arrayObject = new BaseObjectInfo(vm, classInfo, instruction.value);
		arrayObject.setVarValue(vm.getField(classInfo, "length", false), sizeObject);

		BaseObjectInfo[] value = new BaseObjectInfo[size];
		for(int i = 0; i < value.length; i++)
			value[i] = arrayObjectValue;

		arrayObject.value(value);

		context.push(arrayObject);

		return nextIndex;
	}
}
