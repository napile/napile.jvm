package org.napile.vm.objects.classinfo;

import java.util.List;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.invoke.InvokeType;

/**
 * @author VISTALL
 * @date 16:03/31.01.2012
 */
public interface MethodInfo extends ReflectInfo
{
	public static final FqName CONSTRUCTOR_NAME = new FqName("this%CONSTRUCTOR");
	public static final FqName STATIC_CONSTRUCTOR_NAME = new FqName("this%STATIC");

	public static final MethodInfo[] EMPTY_ARRAY = new MethodInfo[0];

	TypeNode getReturnType();

	List<TypeNode> getParameters();

	InvokeType getInvokeType();
}
