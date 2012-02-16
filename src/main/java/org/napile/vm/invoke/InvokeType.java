package org.napile.vm.invoke;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 0:13/17.02.2012
 */
public interface InvokeType
{
	void call(VmInterface vmInterface, InterpreterContext context);
}
