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

package org.napile.vm.objects.classinfo;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.AbstractMemberNode;
import org.napile.asm.tree.members.AnnotationNode;
import org.napile.asm.tree.members.ClassNode;
import org.napile.asm.tree.members.MacroNode;
import org.napile.asm.tree.members.MethodNode;
import org.napile.asm.tree.members.TypeParameterNode;
import org.napile.asm.tree.members.VariableNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.objects.BaseObjectInfo;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 16:01/31.01.2012
 */
public class ClassInfo implements ReflectInfo
{
	private final List<VariableInfo> variableInfos = new ArrayList<VariableInfo>(0);
	private final List<MethodInfo> methodInfos = new ArrayList<MethodInfo>(0);
	private final List<MethodInfo> macroInfos = new ArrayList<MethodInfo>(0);

	public ClassNode classNode;

	private BaseObjectInfo classObjectInfo;
	private BaseObjectInfo objectForStatic;

	public ClassInfo(ClassNode classNode)
	{
		this.classNode = classNode;

		for(AbstractMemberNode<?> memberNode : classNode.getMembers())
		{
			if(memberNode instanceof VariableNode)
				variableInfos.add(new VariableInfo(this, (VariableNode) memberNode));
			else if(memberNode instanceof MacroNode)
			{
				MacroNode methodNode = (MacroNode) memberNode;
				TypeNode[] parameters = new TypeNode[methodNode.parameters.size()];
				for(int i = 0; i < parameters.length; i++)
					parameters[i] = methodNode.parameters.get(i).returnType;

				macroInfos.add(new MethodInfo(this, methodNode.name, methodNode, methodNode.returnType, parameters));
			}
			else if(memberNode instanceof MethodNode)
			{
				MethodNode methodNode = (MethodNode) memberNode;
				TypeNode[] parameters = new TypeNode[methodNode.parameters.size()];
				for(int i = 0; i < parameters.length; i++)
					parameters[i] = methodNode.parameters.get(i).returnType;

				methodInfos.add(new MethodInfo(this, methodNode.name, methodNode, methodNode.returnType, parameters));
			}
		}
	}

	@NotNull
	@Override
	public FqName getFqName()
	{
		return classNode.name;
	}

	public List<TypeParameterNode> getTypeParameters()
	{
		return classNode.typeParameters;
	}

	@Override
	public boolean hasModifier(@NotNull Modifier modifier)
	{
		return ArrayUtil.contains(modifier, classNode.modifiers);
	}

	@Override
	public Modifier[] getModifiers()
	{
		return classNode.modifiers;
	}

	@Override
	public List<AnnotationNode> getAnnotations()
	{
		return classNode.annotations;
	}

	@NotNull
	public List<VariableInfo> getVariables()
	{
		return variableInfos;
	}

	@NotNull
	public List<TypeNode> getExtends()
	{
		return classNode.supers;
	}

	@NotNull
	public List<MethodInfo> getMethods()
	{
		return methodInfos;
	}

	@NotNull
	public List<MethodInfo> getMacros()
	{
		return macroInfos;
	}

	@Override
	public ClassInfo getParent()
	{
		return null;
	}

	@NotNull
	@Override
	public String getName()
	{
		return classNode.name.shortName().getName();
	}

	@Override
	public String toString()
	{
		return getFqName().toString();
	}

	public BaseObjectInfo getClassObjectInfo()
	{
		return classObjectInfo;
	}

	public void setClassObjectInfo(BaseObjectInfo classObjectInfo)
	{
		this.classObjectInfo = classObjectInfo;
	}

	public BaseObjectInfo getObjectForStatic()
	{
		return objectForStatic;
	}

	public void setObjectForStatic(BaseObjectInfo objectForStatic)
	{
		this.objectForStatic = objectForStatic;
	}
}
