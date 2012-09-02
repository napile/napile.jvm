package org.napile.vm.objects.classinfo.impl;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asmNew.Modifier;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Function;

/**
 * @author VISTALL
 * @date 3:03/02.02.2012
 */
public class MethodInfoImpl implements MethodInfo
{
	private List<Modifier> flags = new ArrayList<Modifier>(0);
	private final ClassInfo _parentType;
	private TypeNode returnType;
	private final List<TypeNode> _parameters = new ArrayList<TypeNode>(0);
	private FqName _name;

	private InvokeType invokeType;

	public MethodInfoImpl(ClassInfo parentType, FqName name)
	{
		_parentType = parentType;
		_name = name;
	}

	@NotNull
	@Override
	public FqName getName()
	{
		return _name;
	}

	@NotNull
	@Override
	public List<Modifier> getFlags()
	{
		return flags;
	}

	@Override
	public ClassInfo getParent()
	{
		return _parentType;
	}

	@Override
	public TypeNode getReturnType()
	{
		return returnType;
	}

	@Override
	public List<TypeNode> getParameters()
	{
		return _parameters;
	}

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append(getParent().getName()).append(":");
		if(Flags.isStatic(this))
			b.append("static").append(" ");
		b.append(getName()).append("(");
		b.append(StringUtil.join(_parameters, new Function<TypeNode, String>()
		{
			@Override
			public String fun(TypeNode typeNode)
			{
				return typeNode.toString();
			}
		}, ", "));
		b.append(")");
		b.append(" : ").append(getReturnType()).append(" ");
		return b.toString();
	}

	@Override
	public InvokeType getInvokeType()
	{
		return invokeType;
	}

	public void setReturnType(TypeNode returnType)
	{
		this.returnType = returnType;
	}

	public void setInvokeType(InvokeType invokeType)
	{
		this.invokeType = invokeType;
	}
}
