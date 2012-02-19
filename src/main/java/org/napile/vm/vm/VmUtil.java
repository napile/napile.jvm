package org.napile.vm.vm;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.impl.ArrayClassInfoImpl;
import org.napile.vm.objects.classinfo.impl.PrimitiveClassInfoImpl;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ArrayObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ValueObjectInfo;
import org.napile.vm.objects.objectinfo.impl.primitive.*;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.util.BundleUtil;
import org.napile.vm.util.ClasspathUtil;
import org.napile.vm.util.StringCharReader;

/**
 * @author VISTALL
 * @date 18:27/31.01.2012
 */
public class VmUtil
{
	private static final Logger LOGGER = Logger.getLogger(VmUtil.class);

	public static final ObjectInfo OBJECT_NULL = new ValueObjectInfo<Object>(null, null, null);

	public static void initBootStrap(Vm vm)
	{
		makePrimitiveType(Vm.PRIMITIVE_VOID, vm, null, null);
		makePrimitiveType(Vm.PRIMITIVE_BOOLEAN_ARRAY, vm, BoolObjectInfo.class, Boolean.FALSE);
		makePrimitiveType(Vm.PRIMITIVE_BYTE, vm, ByteObjectInfo.class, (byte)0);
		makePrimitiveType(Vm.PRIMITIVE_SHORT, vm, ShortObjectInfo.class, (short)0);
		makePrimitiveType(Vm.PRIMITIVE_INT, vm, IntObjectInfo.class, 0);
		makePrimitiveType(Vm.PRIMITIVE_LONG, vm, LongObjectInfo.class, (long)0);
		makePrimitiveType(Vm.PRIMITIVE_FLOAT, vm, FloatObjectInfo.class, (float)0);
		makePrimitiveType(Vm.PRIMITIVE_DOUBLE, vm, DoubleObjectInfo.class, (double)0);
		makePrimitiveType(Vm.PRIMITIVE_CHAR, vm, CharObjectInfo.class, (char)0);

		AssertUtil.assertNull(vm.getClass("java.lang.Object"));
		AssertUtil.assertNull(vm.getClass("java.io.Serializable"));
		// for string
		AssertUtil.assertNull(vm.getClass("java.lang.String"));
		// exceptions
		AssertUtil.assertNull(vm.getClass("java.lang.Throwable"));
		AssertUtil.assertNull(vm.getClass("java.lang.Exception"));
		AssertUtil.assertNull(vm.getClass("java.lang.ClassNotFoundException"));

		vm.moveFromBootClassLoader(); // change bootstrap class loader - to new instance
	}

	public static ObjectInfo convertToVm(Vm vm, ClassInfo classInfo, Object object)
	{
		if(classInfo.getName().equals(Vm.PRIMITIVE_BYTE))
			return new ByteObjectInfo(null, classInfo, ((Number)object).byteValue());
		else if(classInfo.getName().equals(Vm.PRIMITIVE_SHORT))
			return new ShortObjectInfo(null, classInfo, ((Number)object).shortValue());
		else if(classInfo.getName().equals(Vm.PRIMITIVE_INT))
			return new IntObjectInfo(null, classInfo, ((Number)object).intValue());
		else if(classInfo.getName().equals(Vm.PRIMITIVE_LONG))
			return new LongObjectInfo(null, classInfo, ((Number)object).longValue());
		else if(classInfo.getName().equals(Vm.PRIMITIVE_FLOAT))
			return new FloatObjectInfo(null, classInfo, ((Number)object).floatValue());
		else if(classInfo.getName().equals(Vm.PRIMITIVE_DOUBLE))
			return new DoubleObjectInfo(null, classInfo, ((Number)object).doubleValue());
		else if(classInfo.getName().equals(Vm.PRIMITIVE_CHAR))
		{
			int val = (Integer)object;
			return new CharObjectInfo(null, classInfo, (char)val);
		}
		else if(classInfo.getName().equals(Vm.JAVA_LANG_STRING))
		{
			ClassInfo primitiveCharClassInfo = vm.getClass(Vm.PRIMITIVE_CHAR);
			ClassInfo primitiveCharClassArrayInfo = vm.getClass(Vm.PRIMITIVE_CHAR_ARRAY);

			char[] data = ((String)object).toCharArray();
			CharObjectInfo[] cData = new CharObjectInfo[data.length];
			for(int i = 0; i < data.length; i++)
				cData[i] = new CharObjectInfo(null, primitiveCharClassInfo, data[i]);

			ArrayObjectInfo arrayObjectInfo = new ArrayObjectInfo(null, primitiveCharClassArrayInfo, cData);

			return AssertUtil.assertNull(vm.newObject(classInfo, new String[]{Vm.PRIMITIVE_CHAR_ARRAY}, arrayObjectInfo));
		}
		else
		{
			System.out.println(classInfo.getName() + " is not convertable. Value: " + object);
		}
			//throw new IllegalArgumentException(classInfo.getName() + " is not convertable. Value: " + object);

		return null;
	}

	public static <T> void makePrimitiveType(String name, Vm vm, Class<? extends ValueObjectInfo<T>> clazz, T value)
	{
		PrimitiveClassInfoImpl classInfo = new PrimitiveClassInfoImpl(name);
		if(clazz != null)
		{
			try
			{
				Constructor<?> constructor = clazz.getConstructor(ObjectInfo.class, ClassInfo.class, value.getClass());
				ObjectInfo objectInfo = (ObjectInfo)constructor.newInstance(null, classInfo, value);
				classInfo.setNullValue(objectInfo);
			}
			catch(Exception e)
			{}

			AssertUtil.assertNull(classInfo.nullValue());
		}
		else
			classInfo.setNullValue(OBJECT_NULL);

		vm.getBootClassLoader().addClassInfo(classInfo);
	}

	public static String canSetValue(ClassInfo left, ClassInfo right)
	{
		if(left == right)                  //object = object
			return null;

		if(right == null)  //object = null
			return null;

		return "Could cast " + right.getName() + " to " + left.getName();
	}

	public static ClassInfo parseType(Vm vm, String val)
	{
		return parseType(vm, new StringCharReader(val));
	}

	public static ClassInfo parseType(Vm vm, StringCharReader charReader)
	{
		char firstChar = charReader.next();
		switch(firstChar)
		{
			case '[':  //array
				int i = 1;//array size
				while(charReader.next() == firstChar)
					i++;

				charReader.back(); //need go back after while

				ClassInfo arrayTypeInfo = parseType(vm, charReader);
				if(arrayTypeInfo == null)
				{
					BundleUtil.exitAbnormal(null, "class.s1.not.found", charReader);
					return null;
				}

				ClassInfo arrayClass = new ArrayClassInfoImpl(arrayTypeInfo, vm.getClass(Vm.JAVA_LANG_OBJECT));
				if(i > 1)
					for(int a = 1; a < i; a++)
						arrayClass = new ArrayClassInfoImpl(arrayClass, vm.getClass(Vm.JAVA_LANG_OBJECT));

				ClassInfo storedClassInfo = vm.getCurrentClassLoader().forName(arrayClass.getName());
				if(storedClassInfo == null)
					vm.getCurrentClassLoader().addClassInfo(arrayClass);
				else
					arrayClass = storedClassInfo;
				return arrayClass;
			//case 'j': //long
			case 'J': //long
				return vm.getBootClassLoader().forName(Vm.PRIMITIVE_LONG);
			case 'C':  //char
				return vm.getBootClassLoader().forName(Vm.PRIMITIVE_CHAR);
			case 'B':  //byte
				return vm.getBootClassLoader().forName(Vm.PRIMITIVE_BYTE);
			case 'D':  //double
				return vm.getBootClassLoader().forName(Vm.PRIMITIVE_DOUBLE);
			case 'F':  //float
				return vm.getBootClassLoader().forName(Vm.PRIMITIVE_FLOAT);
			case 'I':  //int
				return vm.getBootClassLoader().forName(Vm.PRIMITIVE_INT);
			case 'S':  //short
				return vm.getBootClassLoader().forName(Vm.PRIMITIVE_SHORT);
			case 'Z':  //boolean
				return vm.getBootClassLoader().forName(Vm.PRIMITIVE_BOOLEAN_ARRAY);
			case 'V':  //void
				return vm.getBootClassLoader().forName(Vm.PRIMITIVE_VOID);
			case 'T': //generic
				//TODO [VISTALL] make it
				return vm.getBootClassLoader().forName("java.lang.Object");
			case 'L': //class
				StringBuilder b = new StringBuilder();
				while(charReader.next() != ';')
					b.append(charReader.current());

				String text = b.toString().replace("/", ".");
				ClassInfo classInfo = ClasspathUtil.getClassInfoOrParse(vm, text);
				if(classInfo == null)
				{
					BundleUtil.exitAbnormal(null, "class.s1.not.found", text);
					return null;
				}
				else
					return classInfo;
			default:
			{
				LOGGER.error("unknown type: " + firstChar);
				Thread.dumpStack();
			}
		}
		return null;
	}

	public static FieldInfo[] collectAllFields(ClassInfo info)
	{
		List<FieldInfo> list = new ArrayList<FieldInfo>();
		ClassInfo clazz = info;
		while(clazz != null)
		{
			FieldInfo[] fieldInfos = clazz.getFields();
			Collections.addAll(list, fieldInfos);

			clazz = clazz.getSuperClass();
		}

		for(ClassInfo interfaceClass : info.getInterfaces())
		{
			clazz = interfaceClass;
			while(clazz != null)
			{
				FieldInfo[] fieldInfos = clazz.getFields();
				Collections.addAll(list, fieldInfos);
				clazz = clazz.getSuperClass();
			}
		}
		return (list.isEmpty() ? Collections.<FieldInfo>emptyList() : list).toArray(new FieldInfo[list.size()]);
	}

	public static MethodInfo[] collectAllMethods(ClassInfo info)
	{
		List<MethodInfo> list = new ArrayList<MethodInfo>();
		ClassInfo clazz = info;
		while(clazz != null)
		{
			MethodInfo[] fieldInfos = clazz.getMethods();
			Collections.addAll(list, fieldInfos);

			clazz = clazz.getSuperClass();
		}

		return (list.isEmpty() ? Collections.<MethodInfo>emptyList() : list).toArray(new MethodInfo[list.size()]);
	}
}
