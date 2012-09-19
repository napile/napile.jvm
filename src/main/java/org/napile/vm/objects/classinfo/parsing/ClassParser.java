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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.asm.tree.members.types.ClassTypeNode;
import org.napile.asm.tree.members.types.ThisTypeNode;
import org.napile.asm.tree.members.types.TypeConstructorNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.compiler.lang.resolve.name.Name;
import org.napile.vm.invoke.impl.BytecodeInvokeType;
import org.napile.vm.invoke.impl.NativeInvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.ReflectInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.util.ClasspathUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 16:02/31.01.2012
 */
public class ClassParser
{
	private static final Logger LOGGER = Logger.getLogger(ClassParser.class);
	private static final SAXReader SAX_PARSER = new SAXReader(false);

	private final InputStream inputStream;
	private String _name;
	private Vm _vm;

	public ClassParser(Vm vm, InputStream stream, String name)
	{
		_vm = vm;
		inputStream = stream;
		_name = name;
	}

	public FqName parseQuickName() throws IOException
	{
		try
		{
			Document cDocument = SAX_PARSER.read(inputStream);

			String name = cDocument.getRootElement().attributeValue("name");

			AssertUtil.assertNull(name);

			return new FqName(name);
		}
		catch(DocumentException e)
		{
			throw new IOException(e);
		}
	}

	public ClassInfo parse() throws IOException
	{
		try
		{
			Document cDocument = SAX_PARSER.read(inputStream);

			Element rootElement = cDocument.getRootElement();

			FqName className = new FqName(rootElement.attributeValue("name"));
			ClassInfo classInfo = new ClassInfo(className);
			_vm.getCurrentClassLoader().addClassInfo(classInfo); ///need add fist - for circle depends

			Element temp = rootElement.element("extends");

			if(temp != null)
				for(Element e : temp.elements())
					classInfo.getExtends().add(ClasspathUtil.getClassInfoOrParse(_vm, new FqName(e.attributeValue("name"))));

			readModifiers(rootElement, classInfo);

			for(Element e : rootElement.elements("constructor"))
			{
				FqName methodName = className.child(MethodInfo.CONSTRUCTOR_NAME);

				MethodInfo methodInfo = new MethodInfo(classInfo, methodName);

				readModifiers(e, methodInfo);

				classInfo.getMethods().add(methodInfo);

				BytecodeInvokeType bytecodeInvokeType = new BytecodeInvokeType();
				methodInfo.setInvokeType(bytecodeInvokeType);

				Element codeElement = e.element("code");
				if(codeElement != null)
				{
					bytecodeInvokeType.setMaxLocals(Integer.parseInt(codeElement.attributeValue("max_locals")));

					List<Instruction> list = new ArrayList<Instruction>();
					for(Element instrElement : codeElement.elements())
					{
						try
						{
							String opcode = instrElement.getName();
							if(opcode.equals("return"))
								opcode = opcode + "_";

							@SuppressWarnings("unchecked")
							Class<Instruction> instructionClass = (Class<Instruction>)Class.forName("org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl2." + opcode);

							Instruction instruction = instructionClass.newInstance();
							list.add(instruction);

							instruction.parseData(instrElement);
						}
						catch(Exception e1)
						{
							throw new Error(e1);
						}
					}
					bytecodeInvokeType.setInstructions(list.toArray(new Instruction[list.size()]));
				}

				Element parametersElement = e.element("parameters");
				if(parametersElement != null)
					for(Element parameterElement : parametersElement.elements())
					{
						Element typeElement = parameterElement.element("type");

						methodInfo.getParameters().add(parseType(typeElement));
					}
			}

			for(Element e : rootElement.elements("static_constructor"))
			{
				FqName methodName = className.child(MethodInfo.STATIC_CONSTRUCTOR_NAME);

				MethodInfo methodInfo = new MethodInfo(classInfo, methodName);
				methodInfo.getFlags().add(Modifier.STATIC);

				classInfo.getMethods().add(methodInfo);

				BytecodeInvokeType bytecodeInvokeType = new BytecodeInvokeType();
				methodInfo.setInvokeType(bytecodeInvokeType);

				Element codeElement = e.element("code");
				if(codeElement != null)
				{
					bytecodeInvokeType.setMaxLocals(Integer.parseInt(codeElement.attributeValue("max_locals")));

					List<Instruction> list = new ArrayList<Instruction>();
					for(Element instrElement : codeElement.elements())
					{
						try
						{
							String opcode = instrElement.getName();
							if(opcode.equals("return"))
								opcode = opcode + "_";

							@SuppressWarnings("unchecked")
							Class<Instruction> instructionClass = (Class<Instruction>)Class.forName("org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl2." + opcode);

							Instruction instruction = instructionClass.newInstance();
							list.add(instruction);

							instruction.parseData(instrElement);
						}
						catch(Exception e1)
						{
							throw new Error(e1);
						}
					}
					bytecodeInvokeType.setInstructions(list.toArray(new Instruction[list.size()]));
				}
			}

			for(Element e : rootElement.elements("method"))
			{
				FqName methodName = className.child(Name.identifier(e.attributeValue("name")));

				MethodInfo methodInfo = new MethodInfo(classInfo, methodName);

				readModifiers(e, methodInfo);

				if(Flags.isNative(methodInfo))
					methodInfo.setInvokeType(new NativeInvokeType());

				classInfo.getMethods().add(methodInfo);

				Element codeElement = e.element("code");
				if(codeElement != null)
				{
					BytecodeInvokeType bytecodeInvokeType = new BytecodeInvokeType();
					methodInfo.setInvokeType(bytecodeInvokeType);
					bytecodeInvokeType.setMaxLocals(Integer.parseInt(codeElement.attributeValue("max_locals")));

					List<Instruction> list = new ArrayList<Instruction>();
					for(Element instrElement : codeElement.elements())
					{
						try
						{
							String opcode = instrElement.getName();
							if(opcode.equals("return"))
								opcode = opcode + "_";

							@SuppressWarnings("unchecked")
							Class<Instruction> instructionClass = (Class<Instruction>)Class.forName("org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl2." + opcode);

							Instruction instruction = instructionClass.newInstance();
							list.add(instruction);

							instruction.parseData(instrElement);
						}
						catch(Exception e1)
						{
							throw new Error(e1);
						}
					}
					bytecodeInvokeType.setInstructions(list.toArray(new Instruction[list.size()]));
				}

				Element parametersElement = e.element("parameters");
				if(parametersElement != null)
					for(Element parameterElement : parametersElement.elements())
					{
						Element typeElement = parameterElement.element("type");

						methodInfo.getParameters().add(parseType(typeElement));
					}
			}

			for(Element e : rootElement.elements("variable"))
			{
				FqName fieldName = className.child(Name.identifier(e.attributeValue("name")));

				Element returnTypeElement = e.element("return_type");

				TypeNode typeNode = parseType(returnTypeElement.element("type"));

				VariableInfo methodInfo = new VariableInfo(classInfo, typeNode, fieldName);

				readModifiers(e, methodInfo);

				classInfo.getVariables().add(methodInfo);
			}

			return classInfo;
		}
		catch(DocumentException e)
		{
			throw new IOException(e);
		}
	}

	@NotNull
	public static TypeNode parseType(Element e)
	{
		boolean nullable = Boolean.parseBoolean(e.attributeValue("nullable"));
		TypeConstructorNode typeConstructorNode = null;
		for(Element e2 : e.elements())
		{
			if("this".equals(e2.getName()))
				typeConstructorNode = new ThisTypeNode();
			else if("class_type".equals(e2.getName()))
				typeConstructorNode = new ClassTypeNode(new FqName(e2.attributeValue("name")));
		}

		AssertUtil.assertNull(typeConstructorNode);

		return new TypeNode(nullable, typeConstructorNode);
	}

	private void readModifiers(Element parent, ReflectInfo reflectInfo)
	{
		Element modifierList = parent.element("modifiers");
		if(modifierList != null)
			for(Element mod : modifierList.elements())
				reflectInfo.getFlags().add(Modifier.valueOf(mod.getName().toUpperCase()));
	}
}
