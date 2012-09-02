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

package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.objectinfo.impl.BaseObjectInfo;
import org.napile.vm.util.DumpUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 0:16/01.09.12
 */
public class Console
{
	@NativeImplement(className = "Console", methodName = "before", parameters = {})
	public static void before(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		//System.out.println(DumpUtil.dump(objectInfo));
	}

	@NativeImplement(className = "Console", methodName = "write", parameters = {})
	public static void write(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		System.out.println(DumpUtil.dump(objectInfo));
	}
}
