package org.napile.vm.objects.classinfo;

import org.napile.vm.bytecode.Instruction;
import org.napile.vm.objects.classinfo.parsing.variabletable.LocalVariable;

/**
 * @author VISTALL
 * @date 16:03/31.01.2012
 */
public interface MethodInfo extends ReflectInfo
{
	public static final String CONSTRUCTOR_NAME = "<init>";
	public static final String STATIC_CONSTRUCTOR_NAME = "<cinit>";

	public static final MethodInfo[] EMPTY_ARRAY = new MethodInfo[0];

	ClassInfo getReturnType();

	ClassInfo[] getParameters();

	ClassInfo[] getThrowExceptions();

	Instruction[] getInstructions();

	LocalVariable[] getLocalVariables();

	int getMaxLocals();
}
