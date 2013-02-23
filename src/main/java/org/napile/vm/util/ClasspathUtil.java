/*
 * Copyright 2010-2012 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.vm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.napile.asm.resolve.name.FqName;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.parsing.ClassParser;
import org.napile.vm.objects.classinfo.parsing.filemapping.FileMapping;
import org.napile.vm.objects.classinfo.parsing.filemapping.SingleFileMapping;
import org.napile.vm.objects.classinfo.parsing.filemapping.ZippedFileMapping;
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

			checkDirOrFile(vm, f);
		}
	}

	private static void checkDirOrFile(Vm vm, File dir)
	{
		if(!dir.exists())
			return;

		if(dir.isDirectory())
		{
			File[] list = dir.listFiles();
			if(list != null)
				for(File f : list)
					checkDirOrFile(vm, f);
		}
		else
			checkFile(vm, dir);
	}

	private static void checkFile(Vm vm, File file)
	{
		String ext = FileUtil.getFileExtension(file);
		if(ext.equals("nxml"))
		{
			try
			{
				ClassParser parser = new ClassParser(vm, new FileInputStream(file));

				FqName name = parser.parseQuickName();

				vm.getVmContext().addMapping(name, new SingleFileMapping(file));
			}
			catch(IOException e)
			{
				LOGGER.error(e, e);
			}
		}
		else if(ext.equals("nzip"))
		{
			try
			{
				ZipFile zipFile = new ZipFile(file);
				Enumeration<? extends ZipEntry> entryEnumeration = zipFile.entries();
				while(entryEnumeration.hasMoreElements())
				{
					ZipEntry entry = entryEnumeration.nextElement();
					if(FileUtil.getFileExtension(entry.getName()).equals("nxml"))
					{
						String classZipName = entry.getName().replace("/", ".").replace(".nxml", "");

						vm.getVmContext().addMapping(new FqName(classZipName), new ZippedFileMapping(file, entry.getName()));
					}
					/*else if(entry.getName().equals("@module@.xml"))
					{
						SAXReader reader = new SAXReader(false);

						Document document = reader.read(zipFile.getInputStream(entry));


					} */
				}
				zipFile.close();
			}
			catch(Exception e)
			{
				LOGGER.error(e, e);
			}
		}
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
			ClassParser parser = new ClassParser(vm, stream);

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
