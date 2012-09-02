package org.napile.vm.invoke.impl.nativeimpl;

import java.lang.reflect.Method;
import java.util.List;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.compiler.lang.resolve.name.FqName;

/**
 * @author VISTALL
 * @date 0:27/17.02.2012
 */
public class NativeMethodRef
{
	private FqName _name;
	private List<TypeNode> _parameters;
	private Method _method;

	public NativeMethodRef(FqName name, List<TypeNode> params, Method method)
	{
		_name = name;
		_parameters = params;
		_method = method;
	}

	public FqName getName()
	{
		return _name;
	}

	public List<TypeNode> getParameters()
	{
		return _parameters;
	}

	public Method getMethod()
	{
		return _method;
	}
}
