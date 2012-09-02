package org.napile.vm.invoke.impl;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.NativeMethodRef;
import org.napile.vm.invoke.impl.nativeimpl.NativeWrapper;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 0:14/17.02.2012
 */
public class NativeInvokeType implements InvokeType
{
	private static final Logger LOGGER = Logger.getLogger(NativeInvokeType.class);

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		StackEntry entry = context.getLastStack();

		MethodInfo methodInfo = entry.getMethodInfo();
		NativeMethodRef nativeMethodRef = NativeWrapper.getMethod(methodInfo.getParent(), methodInfo.getName(), methodInfo.getParameters());
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
				ObjectInfo objectInfo = (ObjectInfo)method.invoke(null, vm, entry.getObjectInfo(), entry.getArguments());
				if(objectInfo != null)
					context.push(objectInfo);
			}
			catch(Exception e)
			{
				LOGGER.error(e, e);
				System.exit(-1);
			}
		}
	}
}
