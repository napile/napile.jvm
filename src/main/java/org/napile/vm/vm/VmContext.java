package org.napile.vm.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.objects.classinfo.parsing.filemapping.FileMapping;

/**
 * @author VISTALL
 * @date 16:42/31.01.2012
 */
public class VmContext
{
	private FqName _mainClass;

	private Map<FqName, FileMapping> _fileMapping = new HashMap<FqName, FileMapping> ();
	private List<String> _arguments = new ArrayList<String>();

	public VmContext()
	{
		//
	}

	public void addMapping(FqName name, FileMapping fileMapping)
	{
		_fileMapping.put(name, fileMapping);
	}

	public void setMainClass(FqName mainClass)
	{
		_mainClass = mainClass;
	}

	public FqName getMainClass()
	{
		return _mainClass;
	}

	public FileMapping getFileMapping(FqName name)
	{
		return _fileMapping.get(name);
	}

	public List<String> getArguments()
	{
		return _arguments;
	}
}
