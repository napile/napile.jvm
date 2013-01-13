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

package org.napile.vm.objects.classinfo.parsing;

import java.io.IOException;
import java.io.InputStream;

import org.napile.asm.io.xml.in.AsmXmlFileReader;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.AbstractMemberNode;
import org.napile.asm.tree.members.ClassNode;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 16:02/31.01.2012
 */
public class ClassParser
{
	private final InputStream inputStream;

	private Vm _vm;

	public ClassParser(Vm vm, InputStream stream)
	{
		_vm = vm;
		inputStream = stream;
	}

	public FqName parseQuickName() throws IOException
	{
		AsmXmlFileReader reader = new AsmXmlFileReader();

		ClassNode classNode = reader.read(inputStream);

		return classNode.name;
	}

	public ClassInfo parse() throws IOException
	{
		AsmXmlFileReader reader = new AsmXmlFileReader();

		ClassNode classNode = reader.read(inputStream);

		ClassInfo classInfo = new ClassInfo(classNode);
		_vm.getCurrentClassLoader().addClassInfo(classInfo);

		addClasses(classNode);
		return classInfo;
	}

	private void addClasses(ClassNode classNode)
	{
		for(AbstractMemberNode<?> memberNode : classNode.getMembers())
			if(memberNode instanceof ClassNode)
			{
				_vm.getCurrentClassLoader().addClassInfo(new ClassInfo((ClassNode) memberNode));
				addClasses((ClassNode) memberNode);
			}
	}
}
