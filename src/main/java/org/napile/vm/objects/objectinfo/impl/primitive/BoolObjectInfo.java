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

package org.napile.vm.objects.objectinfo.impl.primitive;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.objectinfo.impl.ValueObjectInfo;

/**
 * @author VISTALL
 * @date 18:50/15.02.2012
 */
public class BoolObjectInfo extends ValueObjectInfo<Boolean>
{
	public BoolObjectInfo(ClassInfo classInfo, Boolean value)
	{
		super(classInfo, value);
	}
}
