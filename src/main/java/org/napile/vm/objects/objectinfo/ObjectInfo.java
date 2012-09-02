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

package org.napile.vm.objects.objectinfo;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.impl.ClassObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 18:41/31.01.2012
 */
public abstract class ObjectInfo
{
	public static final ObjectInfo[] EMPTY_ARRAY = new ObjectInfo[0];

	protected ClassObjectInfo _classObjectInfo; // object for 'java.lang.Class'

	public abstract ClassInfo getClassInfo();

	public ClassObjectInfo getClassObjectInfo(Vm vm)
	{
		if(_classObjectInfo == null)
			_classObjectInfo = vm.getClassObjectInfo(getClassInfo());

		return _classObjectInfo;
	}
}
