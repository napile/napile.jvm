package org.napile.jvm.vm;

import java.lang.reflect.Method;

import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.util.AssertUtil;

/**
 * @author VISTALL
 * @date 18:27/31.01.2012
 */
public class VmUtil
{
	public static void initBootStrap(VmInterface vmInterface)
	{
		AssertUtil.assertNull(makeClassInfo(vmInterface, "java.lang.Object"));
		AssertUtil.assertNull(makeClassInfo(vmInterface, "java.io.Serializable"));
		//
		AssertUtil.assertNull(makeClassInfo(vmInterface, "java.lang.Comparable"));
		// for string
		AssertUtil.assertNull(makeClassInfo(vmInterface, "java.lang.CharSequence"));
		AssertUtil.assertNull(makeClassInfo(vmInterface, "java.lang.String"));
		// exceptions
		AssertUtil.assertNull(makeClassInfo(vmInterface, "java.lang.Throwable"));
		AssertUtil.assertNull(makeClassInfo(vmInterface, "java.lang.Exception"));
		AssertUtil.assertNull(makeClassInfo(vmInterface, "java.lang.ClassNotFoundException"));
	}

	public static ClassInfo makeClassInfo(VmInterface vmInterface, String name)
	{
		ClassInfo classInfo = null;

		try
		{
			Class<?> clazz = Class.forName(name);
			ClassInfo superClassInfo = null;
			if(clazz.getSuperclass() != null)
			{
				Class<?> superClazz = clazz.getSuperclass();
				while(superClazz != null)
				{
					AssertUtil.assertNull(vmInterface.getClass(superClazz.getName()));
					superClazz = superClazz.getSuperclass();
				}
				superClassInfo = vmInterface.getClass(clazz.getSuperclass().getName());
			}

			classInfo = new ClassInfo(name);
			classInfo.setSuperClass(superClassInfo);

			for(Method m : clazz.getDeclaredMethods())
				System.out.println(m.getName());
			System.out.println("--------------------");

			vmInterface.getVmContext().addClassInfo(classInfo);

			return classInfo;
		}
		catch(ClassNotFoundException e)
		{
			//
		}

		return classInfo;
	}
}
