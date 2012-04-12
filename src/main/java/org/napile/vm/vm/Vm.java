package org.napile.vm.vm;

import java.util.*;

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
import org.napile.vm.util.CollectionUtil;

/**
 * @author VISTALL
 * @date 17:26/31.01.2012
 */
public class Vm
{
	public static final String PRIMITIVE_VOID = "void";
	public static final String PRIMITIVE_BOOLEAN = "boolean";
	public static final String PRIMITIVE_BOOLEAN_ARRAY = "boolean[]";
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
	public static final String JAVA_LANG_CLASS = "java.lang.Class";
	public static final String JAVA_LANG_STRING = "java.lang.String";
	public static final String JAVA_LANG_STRING_ARRAY = "java.lang.String[]";

	private static final Logger LOGGER = Logger.getLogger(Vm.class);

	private VmContext _vmContext;

	private JClassLoader _bootClassLoader = new SimpleClassLoaderImpl(null);
	private JClassLoader _currentClassLoader = _bootClassLoader;

	private Map<ClassInfo, ClassObjectInfo> _initClasses = new HashMap<ClassInfo, ClassObjectInfo>();

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

	public ClassObjectInfo getClassObjectInfo(ClassInfo classInfo)
	{
		ClassObjectInfo classObjectInfo = _initClasses.get(classInfo);
		if(classObjectInfo == null)
		{
			classObjectInfo = newObject(getClass(JAVA_LANG_CLASS), CollectionUtil.EMPTY_STRING_ARRAY, ObjectInfo.EMPTY_ARRAY);

			_initClasses.put(classInfo, classObjectInfo);
		}

		return classObjectInfo;
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

	public void invoke(MethodInfo methodInfo, ObjectInfo object, InterpreterContext context, ObjectInfo... argument)
	{
		initStatic(methodInfo.getParent(), context);

		if(!methodInfo.getName().equals(MethodInfo.CONSTRUCTOR_NAME))
			AssertUtil.assertTrue(Flags.isStatic(methodInfo) && object != null || !Flags.isStatic(methodInfo) && object == null);

		InvokeType invokeType = methodInfo.getInvokeType();

		AssertUtil.assertNull(invokeType);

		invokeType.call(this, context == null ? new InterpreterContext(new StackEntry(object, methodInfo, argument)) : context);
	}

	public ClassObjectInfo newObject(ClassInfo classInfo, String[] constructorTypes, ObjectInfo... arguments)
	{
		initStatic(classInfo, null);

		MethodInfo methodInfo = AssertUtil.assertNull(getMethod(classInfo, MethodInfo.CONSTRUCTOR_NAME, false, constructorTypes));

		ClassObjectInfo classObjectInfo = new ClassObjectInfo(null, classInfo);

		invoke(methodInfo, classObjectInfo, null, arguments);

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

	private void initStatic(ClassInfo classInfo, InterpreterContext context)
	{
		if(!classInfo.isStaticConstructorCalled())
		{
			List<ClassInfo> subclasses = new ArrayList<ClassInfo>();
			subclasses.add(classInfo);
			ClassInfo subclass = classInfo.getSuperClass();
			while(subclass != null)
			{
				if(!subclasses.contains(subclass))
					subclasses.add(subclass);

				subclass = subclass.getSuperClass();
			}
			Collections.reverse(subclasses);

			for(ClassInfo $classInfo : subclasses)
			{
				if($classInfo.isStaticConstructorCalled())
					continue;

				for(FieldInfo fieldInfo : $classInfo.getFields())
				{
					if(Flags.isStatic(fieldInfo))
					{
						Object o = fieldInfo.getTempValue();
						if(o != null)
						{
							fieldInfo.setValue(VmUtil.convertToVm(this, fieldInfo.getTempValue()));

							fieldInfo.setTempValue(null);
						}
						else
							fieldInfo.setValue(fieldInfo.getType().nullValue());
					}
				}

				$classInfo.setStaticConstructorCalled(true);
				MethodInfo methodInfo = getStaticMethod($classInfo, MethodInfo.STATIC_CONSTRUCTOR_NAME, false);

				if(methodInfo != null)
				{
					StackEntry stackEntry = new StackEntry(null, methodInfo, ObjectInfo.EMPTY_ARRAY);
					InterpreterContext contextMain = /*context == null ?*/ new InterpreterContext(stackEntry);// : context;
					//if(context == null)
					//	contextMain.getStack().add(stackEntry);

					invoke(methodInfo, null, contextMain, ObjectInfo.EMPTY_ARRAY);

					//contextMain.getStack().pollLast();
				}
			}
		}
	}
}
