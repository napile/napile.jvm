package org.napile.jvm.vm;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 17:26/31.01.2012
 */
public interface VmInterface
{
	String PRIMITIVE_VOID = "void";
	String PRIMITIVE_BOOLEAN = "boolean";
	String PRIMITIVE_BYTE = "byte";
	String PRIMITIVE_SHORT = "short";
	String PRIMITIVE_INT = "int";
	String PRIMITIVE_LONG = "long";
	String PRIMITIVE_FLOAT = "float";
	String PRIMITIVE_DOUBLE = "double";
	String PRIMITIVE_CHAR = "char";

	ClassInfo getClass(String name);

	FieldInfo getField(ClassInfo info, String name);

	FieldInfo getStaticField(ClassInfo info, String name);

	MethodInfo getMethod(ClassInfo info, String name, String... params);

	MethodInfo getStaticMethod(ClassInfo info, String name, String... params);

	VmContext getVmContext();
}
