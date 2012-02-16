package org.napile.vm.invoke;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 0:13/17.02.2012
 */
public interface InvokeType
{
	void call(Vm vm, InterpreterContext context);
}
