package org.napile.vm.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asmNew.parsing.type.TypeNodeUtil;
import org.napile.asmNew.util.Comparing2;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.compiler.lang.resolve.name.Name;
import org.napile.compiler.lang.rt.NapileReflectPackage;
import org.napile.vm.classloader.JClassLoader;
import org.napile.vm.classloader.impl.SimpleClassLoaderImpl;
import org.napile.vm.invoke.InvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
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
	private static final Logger LOGGER = Logger.getLogger(Vm.class);

	private VmContext _vmContext;

	private JClassLoader _bootClassLoader = new SimpleClassLoaderImpl(null);
	private JClassLoader _currentClassLoader = _bootClassLoader;

	private Map<ClassInfo, ClassObjectInfo> _initClasses = new HashMap<ClassInfo, ClassObjectInfo>();

	public Vm(VmContext vmContext)
	{
		_vmContext = vmContext;
	}

	public ClassInfo getClass(FqName name)
	{
		return ClasspathUtil.getClassInfoOrParse(this, name);
	}

	public ClassObjectInfo getClassObjectInfo(ClassInfo classInfo)
	{
		ClassObjectInfo classObjectInfo = _initClasses.get(classInfo);
		if(classObjectInfo == null)
		{
			classObjectInfo = newObject(getClass(NapileReflectPackage.CLASS), CollectionUtil.EMPTY_STRING_ARRAY, ObjectInfo.EMPTY_ARRAY);

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

	public MethodInfo getMethod(ClassInfo info, String name, boolean deep, List<TypeNode> params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && !Flags.isStatic(methodInfo) ? methodInfo : null;
	}

	public MethodInfo getStaticMethod(ClassInfo info, String name, boolean deep, List<TypeNode> params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && Flags.isStatic(methodInfo) ? methodInfo : null;
	}

	public MethodInfo getStaticMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		MethodInfo methodInfo = getMethod0(info, name, deep, params);
		return methodInfo != null && Flags.isStatic(methodInfo) ? methodInfo : null;
	}

	public MethodInfo getAnyMethod(ClassInfo info, String name, boolean deep, List<TypeNode> params)
	{
		return getMethod0(info, name, deep, params);
	}

	public MethodInfo getAnyMethod(ClassInfo info, String name, boolean deep, String... params)
	{
		return getMethod0(info, name, deep, params);
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

		MethodInfo methodInfo = AssertUtil.assertNull(getMethod(classInfo, MethodInfo.CONSTRUCTOR_NAME.getFqName(), false, constructorTypes));

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
		FqName fieldName = info.getName().child(Name.identifier(name));

		List<FieldInfo> fieldInfos = deep ? VmUtil.collectAllFields(info) : info.getFields();
		for(FieldInfo fieldInfo : fieldInfos)
			if(fieldInfo.getName().equals(fieldName))
				return fieldInfo;
		return null;
	}

	public static MethodInfo getMethod0(ClassInfo info, String name, boolean deep, String... params)
	{
		List<TypeNode> typeParams = new ArrayList<TypeNode>(params.length);
		for(String param : params)
			typeParams.add(TypeNodeUtil.fromString(param));

		return getMethod0(info, name, deep, typeParams);
	}

	public static MethodInfo getMethod0(ClassInfo info, String name, boolean deep, List<TypeNode> params)
	{
		List<MethodInfo> methodInfos =  deep ? VmUtil.collectAllMethods(info) : info.getMethods();
		FqName methodName = info.getName().child(Name.identifier(name));
		for(MethodInfo methodInfo : methodInfos)
		{
			if(!methodInfo.getName().equals(methodName))
				continue;

			if(!Comparing2.equal(params, methodInfo.getParameters()))
				continue;

			return methodInfo;
		}
		return null;
	}

	private void initStatic(@NotNull ClassInfo classInfo, InterpreterContext context)
	{
		if(!classInfo.isStaticConstructorCalled())
		{
			for(ClassInfo classInfo1 : VmUtil.collectAllClasses(classInfo))
			{
				if(classInfo1.isStaticConstructorCalled())
					continue;

				for(FieldInfo fieldInfo : classInfo1.getFields())
				{
					if(Flags.isStatic(fieldInfo))
						fieldInfo.setValue(VmUtil.OBJECT_NULL);
				}

				classInfo1.setStaticConstructorCalled(true);
				MethodInfo methodInfo = getStaticMethod(classInfo1, MethodInfo.STATIC_CONSTRUCTOR_NAME.getFqName(), false);

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
