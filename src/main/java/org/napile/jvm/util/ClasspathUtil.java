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
import org.napile.jvm.objects.classinfo.parsing.filemapping.FileMapping;
import org.napile.jvm.objects.classinfo.parsing.filemapping.SingleFileMapping;
import org.napile.jvm.objects.classinfo.parsing.filemapping.ZippedFileMapping;
import org.napile.jvm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 20:35/02.02.2012
 */
public class ClasspathUtil
{
	private static final Logger LOGGER = Logger.getLogger(ClasspathUtil.class);

	public static void initClassPath(VmInterface vmInterface, String name)
	{
		String[] split = name.split(";");

		for(String str : split)
		{
			File f = new File(str);
			if(!f.exists())
				continue;

			checkDir(vmInterface, f);
		}
	}

	private static void checkDir(VmInterface vmInterface, File dir)
	{
		if(!dir.exists())
			return;

		if(dir.isDirectory())
		{
			File[] list = dir.listFiles();
			if(list != null)
				for(File f : list)
					checkDir(vmInterface, f);
		}
		else
			checkFile(vmInterface, dir);
	}

	private static void checkFile(VmInterface vmInterface, File file)
	{
		String ext = FileUtils.getFileExtension(file);
		if(ext.equals("class"))
		{
			try
			{
				ClassParser parser = new ClassParser(vmInterface, new FileInputStream(file), file.getName());

				String name = parser.parseQuickName();

				vmInterface.getVmContext().addMapping(name, new SingleFileMapping(file));
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

						vmInterface.getVmContext().addMapping(classZipName, new ZippedFileMapping(file, entry.getName()));
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

	public static ClassInfo getClassInfoOrParse(VmInterface vmInterface, String name)
	{
		ClassInfo classInfo = vmInterface.getCurrentClassLoader().forName(name);
		if(classInfo != null)
			return classInfo;
		FileMapping fileMapping = vmInterface.getVmContext().getFileMapping(name);
		if(fileMapping == null)
			return null;

		return ClasspathUtil.parseClass(vmInterface, fileMapping.openSteam(), fileMapping.getName());
	}

	private static ClassInfo parseClass(VmInterface vmInterface, InputStream stream, String name)
	{
		try
		{
			ClassParser parser = new ClassParser(vmInterface, stream, name);

			ClassInfo classInfo = parser.parse();

			stream.close();

			return classInfo;
		}
		catch(Exception e)
		{
			LOGGER.error("Error while reading file: " + name, e);
			return null;
		}
	}
}
