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
import java.util.List;

import org.dom4j.Element;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.classinfo.parsing.ClassParser;

/**
 * @author VISTALL
 * @date 17:15/02.09.12
 */
public abstract class invoke extends Instruction
{
	protected FqName className;
	protected String methodName;
	protected List<TypeNode> parameters;

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
}
