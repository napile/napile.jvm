package org.napile.jvm.vm.impl;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;
import org.napile.jvm.vm.VmContext;
import org.napile.jvm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 17:36/31.01.2012
 */
public class VmInterfaceImpl implements VmInterface
{
	private VmContext _vmContext;

	public VmInterfaceImpl(VmContext vmContext)
	{
		_vmContext = vmContext;
	}

	@Override
	public ClassInfo getClass(String name)
	{
		return _vmContext.getClassInfoOrParse(name);
	}

	@Override
	public FieldInfo getField(ClassInfo info, String name)
	{
		return null;
	}

	@Override
	public MethodInfo getMethod(ClassInfo info, String name, String... params)
	{
		MethodInfo returnMethod = null;
		MethodInfo[] methodInfos = info.getMethods();
		for(MethodInfo methodInfo : methodInfos)
		{
			if(!methodInfo.getName().equals(name))
				continue;
			ClassInfo[] paramTypes = methodInfo.getParameters();
			if(paramTypes.length != params.length)
				continue;

			loop:
			{
				
				for(int i = 0; i < params.length; i++)
				{
					if(!paramTypes[i].getName().equals(params[i]))
						break loop;
				}

				returnMethod = methodInfo;
			}
		}
		return returnMethod;
	}

	@Override
	public VmContext getVmContext()
	{
		return _vmContext;
	}
}
