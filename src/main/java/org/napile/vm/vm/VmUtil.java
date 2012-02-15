package org.napile.vm.vm;

import java.lang.reflect.Constructor;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.impl.PrimitiveClassInfoImpl;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.*;
import org.napile.vm.util.AssertUtil;

/**
 * @author VISTALL
 * @date 18:27/31.01.2012
 */
public class VmUtil
{
	public static final ObjectInfo<?> OBJECT_NULL = new ObjectInfo<Object>(null, null, null);

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

		vmInterface.newClassLoader(); // change bootstrap class loader - to new instance
	}

	public static <T> void makePrimitiveType(String name, VmInterface vmInterface, Class<? extends ObjectInfo<T>> clazz, T value)
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

	public static boolean isSupported(int major, int minor)
	{
		return major >= 43 && minor >= 0;
	}
}
