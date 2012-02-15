package org.napile.vm.vm.impl;

import org.apache.log4j.Logger;
import org.napile.vm.classloader.JClassLoader;
import org.napile.vm.classloader.impl.SimpleClassLoaderImpl;
import org.napile.vm.interpreter.Interpreter;
import org.napile.vm.interpreter.InterpreterContext;
import org.napile.vm.interpreter.WorkData;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ClassObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.util.ClasspathUtil;
import org.napile.vm.vm.VmContext;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 17:36/31.01.2012
 */
public class VmInterfaceImpl implements VmInterface
{
	private static final Logger LOGGER = Logger.getLogger(VmInterfaceImpl.class);

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
		FieldInfo fieldInfo = getField0(info, name);
		return fieldInfo != null && !Flags.isStatic(fieldInfo) ? fieldInfo : null;
	}

	@Override
	public FieldInfo getStaticField(ClassInfo info, String name)
	{
		FieldInfo fieldInfo = getField0(info, name);
		return fieldInfo != null && Flags.isStatic(fieldInfo) ? fieldInfo : null;
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
	public void invoke(MethodInfo methodInfo, ObjectInfo object, ObjectInfo... argument)
	{
		if(Flags.isNative(methodInfo))
		{
			LOGGER.info("Native methods is not supported: " + methodInfo.toString());
		}
		else
		{
			Interpreter interpreter = new Interpreter(methodInfo.getInstructions(), this);

			interpreter.call(new InterpreterContext(new WorkData(methodInfo, argument)));
		}
	}

	@Override
	public void invokeStatic(MethodInfo methodInfo, ObjectInfo... argument)
	{
		if(Flags.isNative(methodInfo))
		{
			LOGGER.info("Native methods is not supported: " + methodInfo.toString());
		}
		else
		{
			Interpreter interpreter = new Interpreter(methodInfo.getInstructions(), this);

			interpreter.call(new InterpreterContext(new WorkData(methodInfo, argument)));
		}
	}

	@Override
	public ObjectInfo newObject(ClassInfo classInfo, String[] constructorTypes, ObjectInfo... arguments)
	{
		MethodInfo methodInfo = AssertUtil.assertNull(getMethod(classInfo, MethodInfo.CONSTRUCTOR_NAME, constructorTypes));

		ClassObjectInfo classObjectInfo = new ClassObjectInfo(null, classInfo);

		invoke(methodInfo, classObjectInfo, arguments);

		return classObjectInfo;
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

	@Override
	public JClassLoader newClassLoader()
	{
		_currentClassLoader = new SimpleClassLoaderImpl(_currentClassLoader);
		return _currentClassLoader;
	}

	public static FieldInfo getField0(ClassInfo info, String name)
	{
		FieldInfo[] methodInfos = info.getFields();
		for(FieldInfo fieldInfo : methodInfos)
			if(fieldInfo.getName().equals(name))
				return fieldInfo;
		return null;
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
