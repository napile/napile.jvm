package org.napile.vm.objects.classinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 0:25/04.02.2012
 */
public class ArrayClassInfoImpl extends AbstractClassInfo
{
	private ClassInfo _type;
	private ClassInfo _superClass;

	public ArrayClassInfoImpl(ClassInfo type, ClassInfo superClass)
	{
		_type = type;
		_superClass = superClass;

		setNullValue(VmUtil.OBJECT_NULL);
	}

	@Override
	public ClassInfo getParent()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return _type.getName() + "[]";
	}

	@Override
	public int getFlags()
	{
		return 0;
	}

	@Override
	public FieldInfo[] getFields()
	{
		return FieldInfo.EMPTY_ARRAY;
	}

	@Override
	public MethodInfo[] getMethods()
	{
		return MethodInfo.EMPTY_ARRAY;
	}

	@Override
	public ClassInfo getSuperClass()
	{
		return _superClass;
	}

	@Override
	public ClassInfo[] getInterfaces()
	{
		return ClassInfo.EMPTY_ARRAY;
	}

	@Override
	public boolean isStaticConstructorCalled()
	{
		return true;
	}

	@Override
	public void setStaticConstructorCalled(boolean staticInit)
	{

	}
}
