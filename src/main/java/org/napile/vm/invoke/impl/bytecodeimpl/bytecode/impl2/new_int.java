package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl2;

import org.dom4j.Element;
import org.napile.compiler.lang.rt.NapileLangPackage;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.objectinfo.impl.primitive.IntObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 22:22/01.09.12
 */
public class new_int extends Instruction
{
	private int value;

	@Override
	public void parseData(Element element)
	{
		value = Integer.parseInt(element.attributeValue("val"));
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		context.push(new IntObjectInfo(vm.getClass(NapileLangPackage.INT), value));
	}
}
