package org.napile.vm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.parsing.ClassParser;
import org.napile.vm.objects.classinfo.parsing.filemapping.FileMapping;
import org.napile.vm.objects.classinfo.parsing.filemapping.SingleFileMapping;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 20:35/02.02.2012
 */
public class ClasspathUtil
{
	private static final Logger LOGGER = Logger.getLogger(ClasspathUtil.class);

	public static void initClassPath(Vm vm, String name)
	{
		String[] split = name.split(";");

		for(String str : split)
		{
			File f = new File(str);
			if(!f.exists())
				continue;

			checkDir(vm, f);
		}
	}

	private static void checkDir(Vm vm, File dir)
	{
		if(!dir.exists())
			return;

		if(dir.isDirectory())
		{
			File[] list = dir.listFiles();
			if(list != null)
				for(File f : list)
					checkDir(vm, f);
		}
		else
			checkFile(vm, dir);
	}

	private static void checkFile(Vm vm, File file)
	{
		String ext = FileUtil.getFileExtension(file);
		if(ext.equals("xml"))
		{
			try
			{
				ClassParser parser = new ClassParser(vm, new FileInputStream(file), file.getName());

				FqName name = parser.parseQuickName();

				vm.getVmContext().addMapping(name, new SingleFileMapping(file));
			}
			catch(IOException e)
			{
				LOGGER.error(e, e);
			}
		}
		/*else if(ext.equals("jar"))
		{
			try
			{
				ZipFile zipFile = new ZipFile(file);
				Enumeration<? extends ZipEntry> entryEnumeration = zipFile.entries();
				while(entryEnumeration.hasMoreElements())
				{
					ZipEntry entry = entryEnumeration.nextElement();
					if(FileUtil.getFileExtension(entry.getName()).equals("class"))
					{
						String classZipName = entry.getName().replace("/", ".").replace(".class", "");

						vm.getVmContext().addMapping(classZipName, new ZippedFileMapping(file, entry.getName()));
					}
				}
				zipFile.close();
			}
			catch(IOException e)
			{
				LOGGER.error(e, e);
			}
		}   */
	}

	public static ClassInfo getClassInfoOrParse(Vm vm, FqName name)
	{
		ClassInfo classInfo = vm.getCurrentClassLoader().forName(name);
		if(classInfo != null)
			return classInfo;
		FileMapping fileMapping = vm.getVmContext().getFileMapping(name);
		if(fileMapping == null)
			return null;

		return ClasspathUtil.parseClass(vm, fileMapping.openSteam(), fileMapping.getName());
	}

	private static ClassInfo parseClass(Vm vm, InputStream stream, String name)
	{
		try
		{
			ClassParser parser = new ClassParser(vm, stream, name);

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
