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

import java.util.List;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.invoke.InvokeType;

/**
 * @author VISTALL
 * @date 16:03/31.01.2012
 */
public interface MethodInfo extends ReflectInfo
{
	public static final FqName CONSTRUCTOR_NAME = new FqName("this%CONSTRUCTOR");
	public static final FqName STATIC_CONSTRUCTOR_NAME = new FqName("this%STATIC");

	public static final MethodInfo[] EMPTY_ARRAY = new MethodInfo[0];

	TypeNode getReturnType();

	List<TypeNode> getParameters();

	InvokeType getInvokeType();
}
