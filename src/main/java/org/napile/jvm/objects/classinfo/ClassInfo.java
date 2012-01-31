package org.napile.jvm.objects.classinfo;

/**
 * @author VISTALL
 * @date 16:01/31.01.2012
 */
public class ClassInfo
{
	public static final int MAGIC_HEADER = 0xCAFEBABE;

	private FieldInfo[] _fields = FieldInfo.EMPTY_ARRAY;

	private ClassInfo _superClass;
	private String _name;

	public ClassInfo(String name)
	{
		_name = name;
	}

	public String getName()
	{
		return _name;
	}

	public FieldInfo[] getFields()
	{
		return _fields;
	}

	public void setSuperClass(ClassInfo superClass)
	{
		_superClass = superClass;
	}
}
