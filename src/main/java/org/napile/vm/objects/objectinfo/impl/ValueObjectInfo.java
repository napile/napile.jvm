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

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 23:26/15.02.2012
 */
public class ValueObjectInfo<T> extends ObjectInfo
{
	private final ClassInfo _classInfo;

	private T _value;

	public ValueObjectInfo(ClassInfo classInfo, T value)
	{
		super();
		_classInfo = classInfo;
		_value = value;
	}

	@Override
	public ClassInfo getClassInfo()
	{
		return _classInfo;
	}

	public T getValue()
	{
		return _value;
	}

	public void setValue(T value)
	{
		_value = value;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getValue());

		return builder.toString();
	}
}
