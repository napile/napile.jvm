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

package org.napile.vm.invoke.impl.nativeimpl.classes;

import java.io.File;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @since 13:52/10.01.13
 */
public class napile_io_File
{
	@NativeImplement(methodName = "existsImpl", parameters = {"napile.lang.Array<napile.lang.Byte>"})
	public static BaseObjectInfo existsImpl(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		BaseObjectInfo[] array = context.getLastStack().getArguments()[0].value();

		byte[] byteArray = new byte[array.length];
		int i = 0;
		while(i < byteArray.length)
			byteArray[i] = (Byte)array[i ++].value();

		return VmUtil.convertToVm(vm, context, new File(new String(byteArray)).exists());
	}
}
