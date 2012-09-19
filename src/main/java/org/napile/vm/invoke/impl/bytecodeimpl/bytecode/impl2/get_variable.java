package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl2;

import org.dom4j.Element;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.objects.classinfo.parsing.ClassParser;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 0:21/08.09.12
 */
public class get_variable extends Instruction
{
	private TypeNode typeNode;
	private FqName className;
	private String name;

	@Override
	public void parseData(Element element)
	{
		Element varElement = element.element("variable");

		FqName fqName = new FqName(varElement.attributeValue("name"));
		className = fqName.parent();
		name = fqName.shortName().getName();

		typeNode = ClassParser.parseType(varElement.element("type"));
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		ClassInfo classInfo = vm.getClass(className);

		AssertUtil.assertNull(classInfo);

		VariableInfo variableInfo = vm.getField(classInfo, name, true);

		AssertUtil.assertNull(variableInfo);

		BaseObjectInfo value = context.last();

		AssertUtil.assertFalse(value.hasVar(variableInfo));

		context.push(value.getVarValue(variableInfo));
	}
}
