package org.napile.vm.objects.classinfo.impl;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asmNew.Modifier;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 3:01/02.02.2012
 */
public class ClassInfoImpl extends AbstractClassInfo
{
	private final List<FieldInfo> fieldInfos = new ArrayList<FieldInfo>(0);
	private final List<MethodInfo> methodInfos = new ArrayList<MethodInfo>(0);

	private final List<ClassInfo> _extends = new ArrayList<ClassInfo>(0);
	private final FqName _name;
	private final ArrayList<Modifier> flags = new ArrayList<Modifier>(0);

	private boolean _staticConstructorCalled;

	public ClassInfoImpl(FqName name)
	{
		_name = name;

		setNullValue(VmUtil.OBJECT_NULL);
	}

	@NotNull
	@Override
	public FqName getName()
	{
		return _name;
	}

	@NotNull
	@Override
	public List<Modifier> getFlags()
	{
		return flags;
	}

	@NotNull
	@Override
	public List<FieldInfo> getFields()
	{
		return fieldInfos;
	}

	@NotNull
	@Override
	public List<ClassInfo> getExtends()
	{
		return _extends;
	}

	@NotNull
	@Override
	public List<MethodInfo> getMethods()
	{
		return methodInfos;
	}

	@Override
	public ClassInfo getParent()
	{
		return null;
	}

	@Override
	public boolean isStaticConstructorCalled()
	{
		return _staticConstructorCalled;
	}

	@Override
	public void setStaticConstructorCalled(boolean staticConstructorCalled)
	{
		_staticConstructorCalled = staticConstructorCalled;
	}
}
