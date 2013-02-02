package org.napile.vm.objects.classinfo;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.AnnotationNode;
import org.napile.asm.tree.members.MethodParameterNode;
import org.napile.asm.tree.members.types.TypeNode;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @date 18:24/02.02.13
 */
public class CallParameterInfo implements ReflectInfo
{
	private final MethodParameterNode methodParameterNode;

	public CallParameterInfo(MethodParameterNode methodParameterNode)
	{
		this.methodParameterNode = methodParameterNode;
	}

	public TypeNode getReturnType()
	{
		return methodParameterNode.returnType;
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
		return methodParameterNode.name.getName();
	}

	@NotNull
	@Override
	public FqName getFqName()
	{
		return FqName.ROOT;
	}

	@Override
	public boolean hasModifier(@NotNull Modifier modifier)
	{
		return ArrayUtil.contains(modifier, methodParameterNode.modifiers);
	}

	@Override
	public Modifier[] getModifiers()
	{
		return methodParameterNode.modifiers;
	}

	@Override
	public List<AnnotationNode> getAnnotations()
	{
		return methodParameterNode.annotations;
	}
}
