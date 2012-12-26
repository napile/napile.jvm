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

package org.napile.vm.invoke.impl;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.NativeMethodRef;
import org.napile.vm.invoke.impl.nativeimpl.NativeWrapper;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 0:14/17.02.2012
 */
public class NativeInvokeType implements InvokeType
{
	private static final Logger LOGGER = Logger.getLogger(NativeInvokeType.class);

	public static final InvokeType INSTANCE = new NativeInvokeType();

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		StackEntry entry = context.getLastStack();

		MethodInfo methodInfo = entry.getMethodInfo();
		NativeMethodRef nativeMethodRef = NativeWrapper.getMethod(vm, methodInfo.getParent(), methodInfo.getFqName(), methodInfo.getParameters());
		if(nativeMethodRef == null)
		{
			LOGGER.info("NativeMethod not implemented: " + context.getLastStack().getMethodInfo().toString());
			System.exit(-1);
		}
		else
		{
			Method method = nativeMethodRef.getMethod();

			try
			{
				BaseObjectInfo objectInfo = (BaseObjectInfo)method.invoke(null, vm, context);
				if(objectInfo != null)
					entry.setReturnValue(objectInfo);
				else
					entry.setReturnValue(VmUtil.convertToVm(vm, context, null));
			}
			catch(Exception e)
			{
				LOGGER.error(e, e);
				System.exit(-1);
			}
		}
	}
}
