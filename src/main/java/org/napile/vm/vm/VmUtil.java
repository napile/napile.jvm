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

	public static void initBootStrap(VmInterface vmInterface)
	{
		makePrimitiveType(VmInterface.PRIMITIVE_VOID, vmInterface, null, null);
		makePrimitiveType(VmInterface.PRIMITIVE_BOOLEAN, vmInterface, BoolObjectInfo.class, Boolean.FALSE);
		makePrimitiveType(VmInterface.PRIMITIVE_BYTE, vmInterface, ByteObjectInfo.class, (byte)0);
		makePrimitiveType(VmInterface.PRIMITIVE_SHORT, vmInterface, ShortObjectInfo.class, (short)0);
		makePrimitiveType(VmInterface.PRIMITIVE_INT, vmInterface, IntObjectInfo.class, 0);
		makePrimitiveType(VmInterface.PRIMITIVE_LONG, vmInterface, LongObjectInfo.class, (long)0);
		makePrimitiveType(VmInterface.PRIMITIVE_FLOAT, vmInterface, FloatObjectInfo.class, (float)0);
		makePrimitiveType(VmInterface.PRIMITIVE_DOUBLE, vmInterface, DoubleObjectInfo.class, (double)0);
		makePrimitiveType(VmInterface.PRIMITIVE_CHAR, vmInterface, CharObjectInfo.class, (char)0);

		AssertUtil.assertNull(vmInterface.getClass("java.lang.Object"));
		AssertUtil.assertNull(vmInterface.getClass("java.io.Serializable"));
		// for string
		AssertUtil.assertNull(vmInterface.getClass("java.lang.String"));
		// exceptions
		AssertUtil.assertNull(vmInterface.getClass("java.lang.Throwable"));
		AssertUtil.assertNull(vmInterface.getClass("java.lang.Exception"));
		AssertUtil.assertNull(vmInterface.getClass("java.lang.ClassNotFoundException"));

		vmInterface.moveFromBootClassLoader(); // change bootstrap class loader - to new instance
	}

	public static ObjectInfo convertToVm(VmInterface vmInterface, ClassInfo classInfo, Object object)
	{
		if(classInfo.getName().equals(VmInterface.PRIMITIVE_BYTE))
			return new ByteObjectInfo(null, classInfo, ((Number)object).byteValue());
		else if(classInfo.getName().equals(VmInterface.PRIMITIVE_SHORT))
			return new ShortObjectInfo(null, classInfo, ((Number)object).shortValue());
		else if(classInfo.getName().equals(VmInterface.PRIMITIVE_INT))
			return new IntObjectInfo(null, classInfo, ((Number)object).intValue());
		else if(classInfo.getName().equals(VmInterface.PRIMITIVE_LONG))
			return new LongObjectInfo(null, classInfo, ((Number)object).longValue());
		else if(classInfo.getName().equals(VmInterface.PRIMITIVE_FLOAT))
			return new FloatObjectInfo(null, classInfo, ((Number)object).floatValue());
		else if(classInfo.getName().equals(VmInterface.PRIMITIVE_DOUBLE))
			return new DoubleObjectInfo(null, classInfo, ((Number)object).doubleValue());
		else if(classInfo.getName().equals(VmInterface.PRIMITIVE_CHAR))
		{
			int val = (Integer)object;
			return new CharObjectInfo(null, classInfo, (char)val);
		}
		else if(classInfo.getName().equals(VmInterface.JAVA_LANG_STRING))
		{
			ClassInfo primitiveCharClassInfo = vmInterface.getClass(VmInterface.PRIMITIVE_CHAR);
			ClassInfo primitiveCharClassArrayInfo = vmInterface.getClass(VmInterface.PRIMITIVE_CHAR_ARRAY);

			char[] data = ((String)object).toCharArray();
			CharObjectInfo[] cData = new CharObjectInfo[data.length];
			for(int i = 0; i < data.length; i++)
				cData[i] = new CharObjectInfo(null, primitiveCharClassInfo, data[i]);

			ArrayObjectInfo arrayObjectInfo = new ArrayObjectInfo(null, primitiveCharClassArrayInfo, cData);

			return AssertUtil.assertNull(vmInterface.newObject(classInfo, new String[]{VmInterface.PRIMITIVE_CHAR_ARRAY}, arrayObjectInfo));
		}
		else
		{
			System.out.println(classInfo.getName() + " is not convertable. Value: " + object);
		}
			//throw new IllegalArgumentException(classInfo.getName() + " is not convertable. Value: " + object);

		return null;
	}

	public static <T> void makePrimitiveType(String name, VmInterface vmInterface, Class<? extends ValueObjectInfo<T>> clazz, T value)
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

		vmInterface.getBootClassLoader().addClassInfo(classInfo);
	}

	public static boolean canSetValue(ClassInfo left, ClassInfo right)
	{
		if(left == right)                  //object = object
			return true;

		if(left != null && right == null)  //object = null
			return true;

		return false;
	}

	public static ClassInfo parseType(VmInterface vmInterface, String val)
	{
		return parseType(vmInterface, new StringCharReader(val));
	}

	public static ClassInfo parseType(VmInterface vmInterface, StringCharReader charReader)
	{
		char firstChar = charReader.next();
		switch(firstChar)
		{
			case '[':  //array
				int i = 1;//array size
				while(charReader.next() == firstChar)
					i++;

				charReader.back(); //need go back after while

				ClassInfo arrayTypeInfo = parseType(vmInterface, charReader);
				if(arrayTypeInfo == null)
				{
					BundleUtil.exitAbnormal(null, "class.s1.not.found", charReader);
					return null;
				}

				ClassInfo arrayClass = new ArrayClassInfoImpl(arrayTypeInfo, vmInterface.getClass(VmInterface.JAVA_LANG_OBJECT));
				if(i > 1)
					for(int a = 1; a < i; a++)
						arrayClass = new ArrayClassInfoImpl(arrayClass, vmInterface.getClass(VmInterface.JAVA_LANG_OBJECT));

				ClassInfo storedClassInfo = vmInterface.getCurrentClassLoader().forName(arrayClass.getName());
				if(storedClassInfo == null)
					vmInterface.getCurrentClassLoader().addClassInfo(arrayClass);
				else
					arrayClass = storedClassInfo;
				return arrayClass;
			//case 'j': //long
			case 'J': //long
				return vmInterface.getBootClassLoader().forName(VmInterface.PRIMITIVE_LONG);
			case 'C':  //char
				return vmInterface.getBootClassLoader().forName(VmInterface.PRIMITIVE_CHAR);
			case 'B':  //byte
				return vmInterface.getBootClassLoader().forName(VmInterface.PRIMITIVE_BYTE);
			case 'D':  //double
				return vmInterface.getBootClassLoader().forName(VmInterface.PRIMITIVE_DOUBLE);
			case 'F':  //float
				return vmInterface.getBootClassLoader().forName(VmInterface.PRIMITIVE_FLOAT);
			case 'I':  //int
				return vmInterface.getBootClassLoader().forName(VmInterface.PRIMITIVE_INT);
			case 'S':  //short
				return vmInterface.getBootClassLoader().forName(VmInterface.PRIMITIVE_SHORT);
			case 'Z':  //boolean
				return vmInterface.getBootClassLoader().forName(VmInterface.PRIMITIVE_BOOLEAN);
			case 'V':  //void
				return vmInterface.getBootClassLoader().forName(VmInterface.PRIMITIVE_VOID);
			case 'T': //generic
				//TODO [VISTALL] make it
				return vmInterface.getBootClassLoader().forName("java.lang.Object");
			case 'L': //class
				StringBuilder b = new StringBuilder();
				while(charReader.next() != ';')
					b.append(charReader.current());

				String text = b.toString().replace("/", ".");
				ClassInfo classInfo = ClasspathUtil.getClassInfoOrParse(vmInterface, text);
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
