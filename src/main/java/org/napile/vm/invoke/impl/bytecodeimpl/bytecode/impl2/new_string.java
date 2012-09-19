package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl2;

import org.dom4j.Element;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 21:20/19.09.12
 */
public class new_string extends Instruction
{
	private String value;

	@Override
	public void parseData(Element element)
	{
		value = element.attributeValue("val");
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		throw new UnsupportedOperationException();
		//context.push(new StringObjec(vm.getClass(NapileLangPackage.INT), value));
	}
}
