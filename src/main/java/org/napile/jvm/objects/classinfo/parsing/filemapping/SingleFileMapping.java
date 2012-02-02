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
	private String _fileName;

	public SingleFileMapping(String file)
	{
		_fileName = file;
	}

	@Override
	public InputStream openSteam()
	{
		File f = new File(_fileName);
		if(!f.exists())
			return null;
		try
		{
			return new FileInputStream(f);
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
		return _fileName;
	}
}
