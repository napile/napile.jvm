package org.napile.vm.vm;

import org.apache.log4j.Logger;
import org.napile.vm.classloader.JClassLoader;
import org.napile.vm.classloader.impl.SimpleClassLoaderImpl;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.impl.ArrayClassInfoImpl;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ClassObjectInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.util.ClasspathUtil;

/**
 * @author VISTALL
 * @date 17:26/31.01.2012
 */
public class Vm
{
	public static final String PRIMITIVE_VOID = "void";
	public static final String PRIMITIVE_BOOLEAN = "boolean";
	public static final String PRIMITIVE_BYTE = "byte";
	public static final String PRIMITIVE_SHORT = "short";
	public static final String PRIMITIVE_INT = "int";
	public static final String PRIMITIVE_LONG = "long";
	public static final String PRIMITIVE_FLOAT = "float";
	public static final String PRIMITIVE_DOUBLE = "double";
	public static final String PRIMITIVE_CHAR = "char";
	public static final String PRIMITIVE_CHAR_ARRAY = "char[]";
	//
	public static final String JAVA_LANG_OBJECT = "java.lang.Object";
	public static final String JAVA_LANG_STRING = "java.lang.String";
	public static final String JAVA_LANG_STRING_ARRAY = "java.lang.String[]";

	private static final Logger LOGGER = Logger.getLogger(Vm.class);

	private VmContext _vmContext;

	private JClassLoader _bootClassLoader = new SimpleClassLoaderImpl(null);
	private JClassLoader _currentClassLoader = _bootClassLoader;

	public Vm(VmContext vmContext)
	{
		_vmContext = vmContext;
	}

	public ClassInfo getClass(String name)
	{
		ClassInfo classInfo = _currentClassLoader.forName(name);
		if(classInfo == null)
		{
			int index = name.indexOf('[');
			if(index > 0)
			{
				String typeName = name.substring(0, index);

				ClassInfo typeClass = ClasspathUtil.getClassInfoOrParse(this, typeName);
				String data = name.substring(typeName.length(), name.length());
				int size = data.length() / 2; // [] - is 2, TODO [VISTALl] rework for [final]

				ArrayClassInfoImpl arrayClassInfo = new ArrayClassInfoImpl(typeClass, getClass(JAVA_LANG_OBJECT));

				for(int i = 1; i < size; i++)
					arrayClassInfo = new ArrayClassInfoImpl(arrayClassInfo, getClass(JAVA_LANG_OBJECT));

				getCurrentClassLoader().addClassInfo(arrayClassInfo);

				return arrayClassInfo;
			}
			else
				return ClasspathUtil.getClassInfoOrParse(this, name);
		}
		else
			return classInfo;
	}

	public FieldInfo getField(ClassInfo info, String name, boolean deep)
	{
		FieldInfo fieldInfo = getField0(info, name, deep);
		return fieldInfo != null && !Flags.isStatic(fieldInfo) ? fieldInfo : null;
	}

	public FieldInfo getStaticField(ClassInfo info, String name, boolean deep)
	{
		FieldInfo fieldInfo = getField0(info, name, deep);
		return fieldInfo != null && Flags.isStatic(fieldInfo) ? fieldInfo : null;
	}

	public MethodInfo getMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && !Flags.isStatic(methodInfo) ? methodInfo : null;
	}

	public MethodInfo getStaticMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && Flags.isStatic(methodInfo) ? methodInfo : null;
	}

	public void invoke(InterpreterContext context, MethodInfo methodInfo, ObjectInfo object, ObjectInfo... argument)
	{
		AssertUtil.assertTrue(Flags.isStatic(methodInfo) && object != null || !Flags.isStatic(methodInfo) && object == null);

		InvokeType invokeType = methodInfo.getInvokeType();

		AssertUtil.assertNull(invokeType);

		invokeType.call(this, context == null ? new InterpreterContext(new StackEntry(object, methodInfo, argument)) : context);
	}

	public ObjectInfo newObject(ClassInfo classInfo, String[] constructorTypes, ObjectInfo... arguments)
	{
		MethodInfo methodInfo = AssertUtil.assertNull(getMethod(classInfo, MethodInfo.CONSTRUCTOR_NAME, false, constructorTypes));

		ClassObjectInfo classObjectInfo = new ClassObjectInfo(null, classInfo);

		invoke(null, methodInfo, classObjectInfo, arguments);

		return classObjectInfo;
	}

	public VmContext getVmContext()
	{
		return _vmContext;
	}

	public JClassLoader getBootClassLoader()
	{
		return _bootClassLoader;
	}

	public JClassLoader getCurrentClassLoader()
	{
		return _currentClassLoader;
	}

	public JClassLoader moveFromBootClassLoader()
	{
		_currentClassLoader = new SimpleClassLoaderImpl(_currentClassLoader);
		return _currentClassLoader;
	}

	public static FieldInfo getField0(final ClassInfo info, String name, boolean deep)
	{
		FieldInfo[] fieldInfos = deep ? VmUtil.collectAllFields(info) : info.getFields();
		for(FieldInfo fieldInfo : fieldInfos)
			if(fieldInfo.getName().equals(name))
				return fieldInfo;
		return null;
	}

	public static MethodInfo getMethod0(ClassInfo info, String name, boolean deep, String... params)
	{
		MethodInfo[] methodInfos =  deep ? VmUtil.collectAllMethods(info) : info.getMethods();
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

				return methodInfo;
			}
		}
		return null;
	}
}
