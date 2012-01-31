package org.napile.jvm.vm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.napile.jvm.ForDebug;
import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.parsing.ClassParser;

/**
 * @author VISTALL
 * @date 16:42/31.01.2012
 */
public class VmContext
{
	private Map<String, ClassInfo> _classpath = new HashMap<String, ClassInfo>();
	private String _mainClass;

	public VmContext()
	{

	}

	public void initClassPath(String name)
	{
		String[] split = name.split(";");

		for(String str : split)
		{
			File f = new File(str);
			if(!f.exists())
				continue;

			if(f.isFile() && f.getName().endsWith(".class"))    // maybe better
			{
				try
				{
					ClassParser classParser = new ClassParser(new FileInputStream(f));

					ClassInfo classInfo = classParser.parse();
					if(classInfo != null)
						_classpath.put(classInfo.getName(), classInfo);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@ForDebug
	public void print()
	{
		System.out.println("ClassInfo list: ");
		for(String name : _classpath.keySet())
			System.out.println(name);
	}

	public ClassInfo getClassInfo(String name)
	{
		return _classpath.get(name);
	}

	public void addClassInfo(ClassInfo classInfo)
	{
		_classpath.put(classInfo.getName(), classInfo);
	}

	public void setMainClass(String mainClass)
	{
		_mainClass = mainClass;
	}

	public String getMainClass()
	{
		return _mainClass;
	}
}
