package org.napile.jvm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.parsing.ClassParser;
import org.napile.jvm.objects.classinfo.parsing.filemapping.ZippedFileMapping;
import org.napile.jvm.vm.VmContext;

/**
 * @author VISTALL
 * @date 20:35/02.02.2012
 */
public class ClasspathUtil
{
	private static final Logger LOGGER = Logger.getLogger(ClasspathUtil.class);

	public static void initClassPath(VmContext vmContext, String name)
	{
		String[] split = name.split(";");

		for(String str : split)
		{
			File f = new File(str);
			if(!f.exists())
				continue;

			checkDir(vmContext, f);
		}
	}

	private static void checkDir(VmContext vmContext, File dir)
	{
		if(!dir.exists())
			return;

		if(dir.isDirectory())
		{
			File[] list = dir.listFiles();
			if(list != null)
				for(File f : list)
					checkDir(vmContext, f);
		}
		else
			checkFile(vmContext, dir);
	}

	private static void checkFile(VmContext vmContext, File file)
	{
		String ext = FileUtils.getFileExtension(file);
		if(ext.equals("class"))
		{
			try
			{
				parseClass(vmContext, new FileInputStream(file), file.getName());
			}
			catch(IOException e)
			{
				LOGGER.error(e, e);
			} 
		}
		else if(ext.equals("jar"))
		{
			try
			{
				ZipFile zipFile = new ZipFile(file);
				Enumeration<? extends ZipEntry> entryEnumeration = zipFile.entries();
				while(entryEnumeration.hasMoreElements())
				{
					ZipEntry entry = entryEnumeration.nextElement();
					if(FileUtils.getFileExtension(entry.getName()).equals("class"))
					{
						String classZipName = entry.getName().replace("/", ".").replace(".class", "");

						vmContext.addMapping(classZipName, new ZippedFileMapping(file, entry.getName()));
					}
				}
				zipFile.close();
			}
			catch(IOException e)
			{
				LOGGER.error(e, e);
			}
		}
	}

	public static ClassInfo parseClass(VmContext vmContext, InputStream stream, String name)
	{
		try
		{
			ClassParser parser = new ClassParser(stream, name);

			ClassInfo classInfo = parser.parse(vmContext);

			stream.close();

			if(classInfo != null)
				vmContext.addClassInfo(classInfo);

			return classInfo;
		}
		catch(Exception e)
		{
			LOGGER.error("Error while reading file: " + name, e);
			return null;
		}
	}
}
