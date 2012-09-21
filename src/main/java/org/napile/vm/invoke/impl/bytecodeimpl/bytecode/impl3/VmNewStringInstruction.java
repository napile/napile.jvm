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

import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.tree.members.bytecode.impl.NewStringInstruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 19:56/21.09.12
 */
public class VmNewStringInstruction extends VmInstruction<NewStringInstruction>
{
	public VmNewStringInstruction(NewStringInstruction instruction)
	{
		super(instruction);
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		char[] chars = instruction.value.toCharArray();
		BaseObjectInfo arrayObject = vm.newObject(vm.getClass(NapileLangPackage.ARRAY), new String[]{NapileLangPackage.INT.getFqName()}, new BaseObjectInfo(vm, NapileLangPackage.INT).setAttach(chars.length));
		final BaseObjectInfo[] arrayAttach = arrayObject.value(BaseObjectInfo[].class);
		for(int i = 0; i < chars.length; i++)
			arrayAttach[i] = new BaseObjectInfo(vm, NapileLangPackage.CHAR).setAttach(chars[i]);

		BaseObjectInfo stringObject = vm.newObject(vm.getClass(NapileLangPackage.STRING), new String[] {"napile.lang.Array<napile.lang.Char>"}, arrayObject);

		context.push(stringObject);
	}
}
