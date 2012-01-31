package org.napile.jvm.vm.impl;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;
import org.napile.jvm.vm.VmContext;
import org.napile.jvm.vm.VmInterface;
import org.napile.jvm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 17:36/31.01.2012
 */
public class JavaVmInterfaceImpl implements VmInterface
{
	private VmContext _vmContext;

	public JavaVmInterfaceImpl(VmContext vmContext)
	{
		_vmContext = vmContext;
	}

	@Override
	public ClassInfo getClass(String name)
	{
		ClassInfo classInfo = _vmContext.getClassInfo(name);
		if(classInfo != null)
			return classInfo;

		return VmUtil.makeClassInfo(this, name);
	}

	@Override
	public FieldInfo getField(ClassInfo info, String name)
	{
		return null;
	}

	@Override
	public MethodInfo getMethod(ClassInfo info, String name, ClassInfo[] param)
	{
		return null;
	}

	@Override
	public VmContext getVmContext()
	{
		return _vmContext;
	}
}
