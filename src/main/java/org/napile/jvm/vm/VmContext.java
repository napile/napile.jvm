package org.napile.jvm.vm;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.parsing.filemapping.FileMapping;
import org.napile.jvm.util.ClasspathUtil;
import org.napile.jvm.util.ForDebug;

/**
 * @author VISTALL
 * @date 16:42/31.01.2012
 */
public class VmContext
{
	private static final Logger LOGGER = Logger.getLogger(VmContext.class);

	private Map<String, ClassInfo> _classpath = new HashMap<String, ClassInfo>();
	private String _mainClass;

	private Map<String, FileMapping> _fileMapping = new HashMap<String, FileMapping>();

	public VmContext()
	{
		//
	}

	public ClassInfo getClassInfoOrParse(String name)
	{
		ClassInfo classInfo = _classpath.get(name);
		if(classInfo != null)
			return classInfo;

		FileMapping fileMapping = _fileMapping.get(name);
		if(fileMapping == null)
			return null;

		return ClasspathUtil.parseClass(this, fileMapping.openSteam(), fileMapping.getName());
	}

	@ForDebug
	public void print()
	{
		System.out.println("ClassInfo list: ");
		for(String name : _classpath.keySet())
			System.out.println(name);
	}

	public void addClassInfo(ClassInfo classInfo)
	{
		_classpath.put(classInfo.getName(), classInfo);
	}

	public void addMapping(String name, FileMapping fileMapping)
	{
		_fileMapping.put(name, fileMapping);
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
