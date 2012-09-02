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
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 16:13/02.09.12
 */
public class invoke_special extends Instruction
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
		if(parametersElement != null)
			for(Element e : parametersElement.elements())
				parameters.add(ClassParser.parseType(e));

		// TODO [VISTALL] parsing return type
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		StackEntry entry = context.getLastStack();

		ObjectInfo objectInfo = context.last();

		if(objectInfo == VmUtil.OBJECT_NULL)
			objectInfo = null;

		ClassInfo classInfo = AssertUtil.assertNull(vm.getClass(className));

		MethodInfo methodInfo = vm.getAnyMethod(classInfo, methodName, true, parameters);

		AssertUtil.assertNull(methodInfo);

		List<ObjectInfo> arguments = new ArrayList<ObjectInfo>(methodInfo.getParameters().size());
		for(int i = 0; i < methodInfo.getParameters().size(); i++)
			arguments.add(context.last());

		Collections.reverse(arguments);

		StackEntry nextEntry = new StackEntry(objectInfo, methodInfo, arguments.toArray(new ObjectInfo[arguments.size()]));

		context.getStack().add(nextEntry);

		vm.invoke(methodInfo, objectInfo, context, ObjectInfo.EMPTY_ARRAY);

		context.getStack().pollLast();
	}
}
