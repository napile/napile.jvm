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

import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.tree.members.types.ClassTypeNode;
import org.napile.asm.tree.members.types.TypeConstructorNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

/**
* @author VISTALL
* @date 15:28/02.09.12
*/
abstract class SigTypeToTypeNode extends SignatureVisitor
{
	private TypeConstructorNode typeConstructorNode;

	public SigTypeToTypeNode()
	{
		super(Opcodes.ASM4);
	}

	@Override
	public void visitBaseType(char descriptor)
	{
		if(descriptor == 'I')
			typeConstructorNode = new ClassTypeNode(NapileLangPackage.INT);
	}

	public TypeNode getType()
	{
		if(typeConstructorNode == null)
			return null;
		return new TypeNode(false, typeConstructorNode);
	}

	public abstract void visitEnd();
}
