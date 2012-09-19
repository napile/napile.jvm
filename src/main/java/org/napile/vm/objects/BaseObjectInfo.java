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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 23:25/15.02.2012
 */
public final class BaseObjectInfo
{
	public static final BaseObjectInfo[] EMPTY_ARRAY = new BaseObjectInfo[0];

	private BaseObjectInfo classObjectInfo; // object for 'java.lang.Class'
	private Map<VariableInfo, BaseObjectInfo> variables = new HashMap<VariableInfo, BaseObjectInfo>();
	private ClassInfo classInfo;

	private Object attach;

	public BaseObjectInfo(@NotNull Vm vm, @NotNull FqName fqName)
	{
		this(vm, vm.getClass(fqName));
	}

	public BaseObjectInfo(@NotNull Vm vm, @NotNull ClassInfo classInfo)
	{
		this.classInfo = classInfo;

		List<VariableInfo> variableInfos = VmUtil.collectAllFields(vm, classInfo);

		for(VariableInfo f : variableInfos)
		{
			if(f.getFlags().contains(Modifier.STATIC))
				continue;

			variables.put(f, null);
		}
	}

	public ClassInfo getClassInfo()
	{
		return classInfo;
	}

	public BaseObjectInfo getClassObjectInfo(Vm vm)
	{
		if(classObjectInfo == null)
			classObjectInfo = vm.getClassObjectInfo(getClassInfo());

		return classObjectInfo;
	}

	public BaseObjectInfo getVarValue(@NotNull VariableInfo variableInfo)
	{
		return variables.get(variableInfo);
	}

	public boolean hasVar(VariableInfo variableInfo)
	{
		return variables.containsKey(variableInfo);
	}

	public void setVarValue(@NotNull VariableInfo varValue, @NotNull BaseObjectInfo value)
	{
		variables.put(varValue, value);
	}

	@Override
	public String toString()
	{
		return "Object of " + classInfo.toString() + " attach: " + attach;
	}

	public Object getAttach()
	{
		return attach;
	}

	public BaseObjectInfo setAttach(Object attach)
	{
		this.attach = attach;
		return this;
	}
}
