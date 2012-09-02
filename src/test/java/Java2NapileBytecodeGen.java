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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jetbrains.annotations.NotNull;
import org.napile.asm.AsmBuilder;
import org.napile.asm.Modifier;
import org.napile.asm.tree.members.ClassNode;
import org.napile.asm.tree.members.MethodNode;
import org.napile.asm.tree.members.bytecode.MethodRef;
import org.napile.asm.tree.members.bytecode.VariableRef;
import org.napile.asm.tree.members.bytecode.impl.*;
import org.napile.asm.tree.members.types.ClassTypeNode;
import org.napile.asm.tree.members.types.TypeConstructorNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.writters.BytecodeToXmlWriter;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.compiler.lang.resolve.name.Name;
import org.napile.compiler.lang.rt.NapileLangPackage;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * @author VISTALL
 * @date 14:06/02.09.12
 */
public class Java2NapileBytecodeGen
{

	public static void main(String... arg) throws Exception
	{
		File files = new File("out/test/napile.jvm");
		final AsmBuilder classBuilder = new AsmBuilder();

		for(File file : files.listFiles())
		{
			// stupied hack - dont generate self class
			if(file.getName().contains(Java2NapileBytecodeGen.class.getSimpleName()) || file.getName().contains(SigTypeToTypeNode.class.getSimpleName()))
				continue;

			ClassReader classReader = new ClassReader(new FileInputStream(file));

			classReader.accept(new ClassVisitor(Opcodes.ASM4)
			{
				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
				{
					List<Modifier> modifiers = new ArrayList<Modifier>();
					if(java.lang.reflect.Modifier.isStatic(access))
						modifiers.add(Modifier.STATIC);

					ClassNode classNode = classBuilder.visitClass(modifiers.toArray(new Modifier[modifiers.size()]), new FqName(name));

					classNode.visitSuper(new FqName(normalizeFq(superName)));
				}

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
				{
					TypeConstructorNode typeConstructorNode = null;
					if(desc.charAt(0) == 'I')
						typeConstructorNode = new ClassTypeNode(NapileLangPackage.INT);

					classBuilder.visitVariable(Modifier.EMPTY, name).returnType = new TypeNode(false, typeConstructorNode);
					return super.visitField(access, name, desc, signature, value);
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
				{
					List<Modifier> modifiers = new ArrayList<Modifier>();
					if(java.lang.reflect.Modifier.isStatic(access))
						modifiers.add(Modifier.STATIC);

					name = normalizeMethod(name);

					final MethodNode methodNode = classBuilder.visitMethod(modifiers.toArray(new Modifier[modifiers.size()]), name);

					return new MethodVisitor(Opcodes.ASM4)
					{
						@Override
						public void visitMaxs(int maxStack, int maxLocals)
						{
							methodNode.visitMaxs(maxStack, maxLocals);
						}

						@Override
						public void visitInsn(int opcode)
						{
							if(opcode == Opcodes.DUP)
								methodNode.instructions.add(new DupInstruction());
							else if(opcode == Opcodes.RETURN)
								methodNode.instructions.add(new ReturnInstruction());
						}

						@Override
						public void visitVarInsn(int opcode, int var)
						{
							switch(opcode)
							{
								case Opcodes.ASTORE:
									methodNode.instructions.add(new StoreInstruction(var));
									break;
								case Opcodes.ALOAD:
									methodNode.instructions.add(new LoadInstruction(var));
									break;
							}
						}

						@Override
						public void visitIntInsn(int opcode, int operand)
						{
							// only new-int
							if(opcode == Opcodes.BIPUSH || opcode == Opcodes.SIPUSH)
								methodNode.instructions.add(new NewIntInstruction(operand));
						}

						@Override
						public void visitFieldInsn(int opcode, String owner, String name, String desc)
						{
							owner = normalizeFq(owner);

							TypeNode typeNode = null;
							if(desc.charAt(0) == 'I')
								typeNode = classBuilder.createTypeOfClass(NapileLangPackage.INT);

							VariableRef variableRef = new VariableRef(new FqName(owner).child(Name.identifier(name)), typeNode);
							if(opcode == Opcodes.PUTFIELD)
								methodNode.instructions.add(new PutToVariableInstruction(variableRef));
							else if(opcode == Opcodes.PUTSTATIC)
								methodNode.instructions.add(new PutToStaticVariableInstruction(variableRef));
						}

						@Override
						public void visitMethodInsn(int opcode, String owner, String name, String desc)
						{
							owner = normalizeFq(owner);
							name = normalizeMethod(name);
							FqName mName = new FqName(owner).child(Name.identifier(name));

							final TypeNode[] returnType = new TypeNode[1];
							final List<TypeNode> parameters = new ArrayList<TypeNode>();

							SignatureReader signatureReader = new SignatureReader(desc);
							signatureReader.accept(new SignatureVisitor(Opcodes.ASM4)
							{
								@Override
								public SignatureVisitor visitReturnType()
								{
									return new SigTypeToTypeNode()
									{
										@Override
										public void visitEnd()
										{
											returnType[0] = getType();
										}
									};
								}

								@Override
								public SignatureVisitor visitParameterType()
								{
									return new SigTypeToTypeNode()
									{
										@Override
										public void visitEnd()
										{
											TypeNode typeNode = getType();
											if(typeNode != null)
												parameters.add(typeNode);
										}
									};
								}
							});

							MethodRef methodRef = new MethodRef(mName, parameters, returnType[0]);

							switch(opcode)
							{
								case Opcodes.INVOKESPECIAL:
									methodNode.instructions.add(new InvokeSpecialInstruction(methodRef));
									break;
								case Opcodes.INVOKEVIRTUAL:
									methodNode.instructions.add(new InvokeVirtualInstruction(methodRef));
									break;
								case Opcodes.INVOKESTATIC:
									methodNode.instructions.add(new InvokeStaticInstruction(methodRef));
									break;
							}
						}

						@Override
						public void visitTypeInsn(int opcode, String type)
						{
							type = normalizeFq(type);

							if(opcode == Opcodes.NEW)
								methodNode.instructions.add(new NewObjectInstruction(classBuilder.createTypeOfClass(new FqName(type))));
						}
					};
				}
			}, 0);
		}

		classBuilder.getResult(new BytecodeToXmlWriter<File>()
		{
			@NotNull
			@Override
			public File getResult()
			{
				Element rootElement = document.getRootElement();
				File f = new File("dist/classpath", rootElement.attributeValue("name") + ".xml");
				if(f.exists())
					f.delete();

				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setIndent("\t");
				try
				{
					XMLWriter writer = new XMLWriter(new FileOutputStream(f), format);
					writer.write(document);
					writer.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}

				return f;
			}
		});
	}

	private static String normalizeFq(String str)
	{
		return toNapileClasses(str.replace("/", "."));
	}

	private static String toNapileClasses(String str)
	{
		if(str.equals("java.lang.Object"))
			return NapileLangPackage.ANY.toString();
		else
			return str;
	}

	private static String normalizeMethod(String str)
	{
		if(str.equals("<init>"))
			str = MethodInfo.CONSTRUCTOR_NAME.getFqName();
		else if(str.equals("<clinit>"))
			str = MethodInfo.STATIC_CONSTRUCTOR_NAME.getFqName();
		return str;
	}
}
