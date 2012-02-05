package org.napile.jvm.objects.classinfo.parsing.filemapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author VISTALL
 * @date 19:26/02.02.2012
 */
public class SingleFileMapping implements FileMapping
{
	private File _file;

	public SingleFileMapping(File file)
	{
		_file = file;
	}

	@Override
	public InputStream openSteam()
	{
		if(!_file.exists())
			return null;
		try
		{
			return new FileInputStream(_file);
		}
		catch(FileNotFoundException e)
		{
			//
		}
		return null;
	}

	@Override
	public String getName()
	{
		return _file.getAbsolutePath();
	}
}
