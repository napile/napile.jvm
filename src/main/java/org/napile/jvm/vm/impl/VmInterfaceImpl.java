package org.napile.jvm.vm.impl;

import org.napile.jvm.classloader.JClassLoader;
import org.napile.jvm.classloader.impl.SimpleClassLoaderImpl;
import org.napile.jvm.objects.Flags;
import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;
import org.napile.jvm.util.ClasspathUtil;
import org.napile.jvm.vm.VmContext;
import org.napile.jvm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 17:36/31.01.2012
 */
public class VmInterfaceImpl implements VmInterface
{
	private VmContext _vmContext;

	private JClassLoader _bootClassLoader = new SimpleClassLoaderImpl(null);
	private JClassLoader _currentClassLoader = _bootClassLoader;

	public VmInterfaceImpl(VmContext vmContext)
	{
		_vmContext = vmContext;
	}

	@Override
	public ClassInfo getClass(String name)
	{
		ClassInfo classInfo = _currentClassLoader.forName(name);

		return classInfo == null ? ClasspathUtil.getClassInfoOrParse(this, name) : classInfo;
	}

	@Override
	public FieldInfo getField(ClassInfo info, String name)
	{
		return null;
	}

	@Override
	public FieldInfo getStaticField(ClassInfo info, String name)
	{
		return null;
	}

	@Override
	public MethodInfo getMethod(ClassInfo info, String name, String... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, params);
		return methodInfo != null && !Flags.isStatic(methodInfo) ? methodInfo : null;
	}

	@Override
	public MethodInfo getStaticMethod(ClassInfo info, String name, String... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, params);
		return methodInfo != null && Flags.isStatic(methodInfo) ? methodInfo : null;
	}

	@Override
	public VmContext getVmContext()
	{
		return _vmContext;
	}

	@Override
	public JClassLoader getBootClassLoader()
	{
		return _bootClassLoader;
	}

	@Override
	public JClassLoader getCurrentClassLoader()
	{
		return _currentClassLoader;
	}

	public static MethodInfo getMethod0(ClassInfo info, String name, String... params)
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
}
