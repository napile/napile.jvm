package org.napile.vm.objects.classinfo;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asmNew.Modifier;
import org.napile.compiler.lang.resolve.name.FqName;

/**
 * @author VISTALL
 * @date 3:03/02.02.2012
 */
public interface ReflectInfo
{
	ClassInfo getParent();

	@NotNull
	FqName getName();

	@NotNull
	List<Modifier> getFlags();
}
