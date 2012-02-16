package org.napile.vm.objects.classinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 3:01/02.02.2012
 */
public class ClassInfoImpl extends AbstractClassInfo
{
	private ClassInfo[] _interfaces = ClassInfo.EMPTY_ARRAY;
	private FieldInfo[] _fields = FieldInfo.EMPTY_ARRAY;
	private MethodInfo[] _methods = MethodInfo.EMPTY_ARRAY;

	private ConstantPool _constantPool;
	private ClassInfo _superClass;
	private final String _name;
	private final int _flags;

	public ClassInfoImpl(ConstantPool constantPool, String name, int flags)
	{
		_constantPool = constantPool;
		_name = name;
		_flags = flags;

		setNullValue(VmUtil.OBJECT_NULL);
	}

	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public int getFlags()
	{
		return _flags;
	}

	@Override
	public FieldInfo[] getFields()
	{
		return _fields;
	}

	@Override
	public ClassInfo getSuperClass()
	{
		return _superClass;
	}

	@Override
	public ClassInfo[] getInterfaces()
	{
		return _interfaces;
	}

	@Override
	public MethodInfo[] getMethods()
	{
		return _methods;
	}

	@Override
	public ConstantPool getConstantPool()
	{
		return _constantPool;
	}

	public void setSuperClass(ClassInfo superClass)
	{
		_superClass = superClass;
	}

	public void setInterfaces(ClassInfo[] interfaces)
	{
		_interfaces = interfaces;
	}

	public void setFields(FieldInfo[] fs)
	{
		_fields = fs;
	}

	public void setMethods(MethodInfo[] methods)
	{
		_methods = methods;
	}

	@Override
	public ClassInfo getParent()
	{
		return null;
	}
}
