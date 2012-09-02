package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dom4j.Element;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.ClassParser;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 22:24/01.09.12
 */
public class invoke_static extends Instruction
{
	private FqName className;
	private String methodName;
	private List<TypeNode> parameters;

	@Override
	public void parseData(Element b)
	{
		Element methodElement = b.element("method");

		FqName fqName = new FqName(methodElement.attributeValue("name"));
		className = fqName.parent();
		methodName = fqName.shortName().getName();

		parameters = new ArrayList<TypeNode>(5);

		Element parametersElement = methodElement.element("parameters");
		// parameters / type
		for(Element e : parametersElement.elements())
			parameters.add(ClassParser.parseType(e));
		// TODO [VISTALL] parsing return type
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		ClassInfo classInfo = AssertUtil.assertNull(vm.getClass(className));

		MethodInfo methodInfo = vm.getAnyMethod(classInfo, methodName, true, parameters);

		AssertUtil.assertNull(methodInfo);

		List<ObjectInfo> arguments = new ArrayList<ObjectInfo>(methodInfo.getParameters().size());
		for(int i = 0; i < methodInfo.getParameters().size(); i++)
			arguments.add(context.last());

		Collections.reverse(arguments);

		StackEntry nextEntry = new StackEntry(null, methodInfo, arguments.toArray(new ObjectInfo[arguments.size()]));

		context.getStack().add(nextEntry);

		vm.invoke(methodInfo, null, context, ObjectInfo.EMPTY_ARRAY);

		context.getStack().pollLast();
	}
}
