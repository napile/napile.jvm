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

package org.napile.vm.objects.objectinfo.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 23:25/15.02.2012
 */
public class ClassObjectInfo  extends ObjectInfo
{
	private Map<FieldInfo, ObjectInfo> _fields = new HashMap<FieldInfo, ObjectInfo>();
	private ClassInfo _classInfo;

	public ClassObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo)
	{
		super();
		_classInfo = classInfo;

		List<FieldInfo> fieldInfos = VmUtil.collectAllFields(classInfo);

		for(FieldInfo f : fieldInfos)
		{
			if(Flags.isStatic(f))
				continue;

			_fields.put(f, VmUtil.OBJECT_NULL);
		}
	}

	@Override
	public ClassInfo getClassInfo()
	{
		return _classInfo;
	}

	public ObjectInfo getValueOfVariable(@NotNull Vm vm, @NotNull String name)
	{
		FieldInfo fieldInfo = vm.getAnyField(_classInfo, name, true);
		if(fieldInfo == null)
			return null;
		return _fields.get(fieldInfo);
	}

	public Map<FieldInfo, ObjectInfo> getFields()
	{
		return _fields;
	}

	@Override
	public String toString()
	{
		return "Object of " + _classInfo.toString();
	}
}
