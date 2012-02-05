package org.napile.jvm.objects.classinfo;

/**
 * @author VISTALL
 * @date 3:03/02.02.2012
 */
public interface ReflectInfo
{
	public static final String ATT_CONSTANT_VALUE = "ConstantValue";
	public static final String ATT_SIGNATURE = "Signature";
	public static final String ATT_CODE = "Code";
	public static final String ATT_EXCEPTIONS = "Exceptions";
	public static final String ATT_LINE_NUMBER_TABLE = "LineNumberTable";
	public static final String ATT_LOCAL_VARIABLE_TABLE = "LocalVariableTable";
	public static final String ATT_STACK_MAP_TABLE = "StackMapTable";
	public static final String ATT_DEPRECATED = "Deprecated";
	public static final String ATT_RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";

	String getName();

	int getFlags();
}
