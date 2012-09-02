package org.napile.vm.objects.classinfo.impl;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asmNew.Modifier;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 3:03/02.02.2012
 */
public class FieldInfoImpl implements FieldInfo
{
	private List<Modifier> flags = new ArrayList<Modifier>(0);
	private ClassInfo _parent;
	private TypeNode _type;
	private FqName _name;

	private ObjectInfo _value;

	public FieldInfoImpl(ClassInfo parent, TypeNode type, FqName name)
	{
		_parent = parent;
		_type = type;
		_name = name;
	}

	@Override
	public ClassInfo getParent()
	{
		return _parent;
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
	public void setValue(ObjectInfo value)
	{
		_value = value;
	}

	@Override
	public ObjectInfo getValue()
	{
		return _value;
	}

	@Override
	public TypeNode getType()
	{
		return _type;
	}

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append(getParent().getName()).append(":");
		b.append(_type.toString()).append(" ");
		b.append(getName());
		return b.toString();
	}
}
