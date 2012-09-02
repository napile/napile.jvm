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

package org.napile.vm.objects;

import org.napile.asm.Modifier;
import org.napile.vm.objects.classinfo.ReflectInfo;

/**
 * @author VISTALL
 * @date 9:54/05.02.2012
 */
public class Flags
{
	public static boolean isNative(ReflectInfo reflectInfo)
	{
		return reflectInfo.getFlags().contains(Modifier.NATIVE);
	}

	public static boolean isStatic(ReflectInfo reflectInfo)
	{
		return reflectInfo.getFlags().contains(Modifier.STATIC);
	}
}
