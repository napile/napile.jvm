package org.napile.jvm.objects.classinfo.parsing;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.napile.jvm.objects.classinfo.ClassInfo;

/**
 * @author VISTALL
 * @date 16:02/31.01.2012
 */
public class ClassParser
{
	private static final Logger LOGGER = Logger.getLogger(ClassParser.class);

	private final DataInputStream _dataInputStream;

	public ClassParser(InputStream stream)
	{
		_dataInputStream = new DataInputStream(stream);
	}

	public ClassInfo parse() throws IOException
	{
		int magic = _dataInputStream.readInt();
		if(magic != ClassInfo.MAGIC_HEADER)
		{

		}
		return null;
	}
}
