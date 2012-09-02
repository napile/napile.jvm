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
