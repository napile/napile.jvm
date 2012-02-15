package org.napile.vm.vm;

import java.util.HashMap;
import java.util.Map;

import org.napile.vm.objects.classinfo.parsing.filemapping.FileMapping;

/**
 * @author VISTALL
 * @date 16:42/31.01.2012
 */
public class VmContext
{
	private String _mainClass;

	private Map<String, FileMapping> _fileMapping = new HashMap<String, FileMapping> ();

	public VmContext()
	{
		//
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

	public FileMapping getFileMapping(String name)
	{
		return _fileMapping.get(name);
	}
}
