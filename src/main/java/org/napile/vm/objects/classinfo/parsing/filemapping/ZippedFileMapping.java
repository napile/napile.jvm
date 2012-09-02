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
