package org.napile.vm.vm;

import org.napile.vm.classloader.JClassLoader;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

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
	String JAVA_LANG_STRING = "java.lang.String";

	ClassInfo getClass(String name);

	FieldInfo getField(ClassInfo info, String name);

	FieldInfo getStaticField(ClassInfo info, String name);

	MethodInfo getMethod(ClassInfo info, String name, String... params);

	MethodInfo getStaticMethod(ClassInfo info, String name, String... params);

	void invoke(MethodInfo methodInfo, ObjectInfo object, ObjectInfo... argument);

	void invokeStatic(MethodInfo methodInfo, ObjectInfo... argument);

	VmContext getVmContext();

	JClassLoader getBootClassLoader();

	JClassLoader getCurrentClassLoader();

	JClassLoader newClassLoader();
}
