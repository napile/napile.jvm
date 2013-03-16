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

/**
 * @author VISTALL
 * @since 2:57/02.02.2012
 */
public class FileUtil
{
	public static String getFileExtension(File file)
	{
		return getFileExtension(file.getName());
	}

	public static String getFileExtension(String name)
	{
		int dot = name.lastIndexOf('.');
		return name.substring(dot + 1);
	}
}
