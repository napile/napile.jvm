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

package org.napile.vm.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.objects.classinfo.parsing.filemapping.FileMapping;

/**
 * @author VISTALL
 * @date 16:42/31.01.2012
 */
public class VmContext
{
	private FqName _mainClass;

	private Map<FqName, FileMapping> _fileMapping = new HashMap<FqName, FileMapping> ();
	private List<String> _arguments = new ArrayList<String>();

	public VmContext()
	{
		//
	}

	public void addMapping(FqName name, FileMapping fileMapping)
	{
		_fileMapping.put(name, fileMapping);
	}

	public void setMainClass(FqName mainClass)
	{
		_mainClass = mainClass;
	}

	public FqName getMainClass()
	{
		return _mainClass;
	}

	public FileMapping getFileMapping(FqName name)
	{
		return _fileMapping.get(name);
	}

	public List<String> getArguments()
	{
		return _arguments;
	}
}
