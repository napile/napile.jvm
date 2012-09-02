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

import org.dom4j.Element;
import org.napile.asm.tree.members.types.ClassTypeNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.parsing.ClassParser;
import org.napile.vm.objects.objectinfo.impl.ClassObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 16:16/02.09.12
 */
public class new_object extends Instruction
{
	private TypeNode typeNode;

	@Override
	public void parseData(Element element)
	{
		typeNode = ClassParser.parseType(element.element("type"));
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		ClassTypeNode classTypeNode = (ClassTypeNode) typeNode.typeConstructorNode;

		ClassInfo classInfo = vm.getClass(classTypeNode.getClassName());

		ClassObjectInfo classObjectInfo = new ClassObjectInfo(null, classInfo);

		context.push(classObjectInfo);
	}
}
