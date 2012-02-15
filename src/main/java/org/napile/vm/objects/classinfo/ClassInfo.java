package org.napile.vm.objects.classinfo;

import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 16:01/31.01.2012
 */
public interface ClassInfo extends ReflectInfo
{
	public static final int MAGIC_HEADER = 0xCAFEBABE;

	public static final ClassInfo[] EMPTY_ARRAY = new ClassInfo[0];

	public String getName();

	public FieldInfo[] getFields();

	public MethodInfo[] getMethods();

	public ClassInfo getSuperClass();

	public ClassInfo[] getInterfaces();

	public ObjectInfo nullValue();

	public ConstantPool getConstantPool();
}
