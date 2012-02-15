package org.napile.vm.objects.classinfo.parsing.filemapping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author VISTALL
 * @date 19:27/02.02.2012
 */
public class ZippedFileMapping implements FileMapping
{
	private File _zipFile;
	private String _entryName;

	public ZippedFileMapping(File zipFile, String entryName)
	{
		_zipFile = zipFile;
		_entryName = entryName;
	}

	@Override
	public InputStream openSteam()
	{
		ZipFile zipFile = null;
		try
		{
			zipFile = new ZipFile(_zipFile);
			ZipEntry entry = zipFile.getEntry(_entryName);

			return zipFile.getInputStream(entry);
		}
		catch(IOException e)
		{
			return null;
		}
	}

	@Override
	public String getName()
	{
		return _zipFile.getAbsolutePath() + "$" + _entryName;
	}
}
