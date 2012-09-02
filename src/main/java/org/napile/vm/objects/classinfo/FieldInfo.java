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

package org.napile.vm.objects.classinfo;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 16:03/31.01.2012
 */
public interface FieldInfo extends ReflectInfo
{
	public static final FieldInfo[] EMPTY_ARRAY = new FieldInfo[0];

	void setValue(ObjectInfo value);

	ObjectInfo getValue();

	TypeNode getType();
}
