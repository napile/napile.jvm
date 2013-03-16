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

import java.lang.reflect.Array;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.Modifier;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @since 23:20/06.02.2012
 */
public class DumpUtil
{
	public static String dump(@NotNull Vm vm, @NotNull BaseObjectInfo objectInfo)
	{
		ClassInfo classInfo = objectInfo.getClassInfo();
		StringBuilder builder = new StringBuilder();
		builder.append("Object dump: ").append(objectInfo.hashCode()).append(", class: ").append(classInfo.getFqName()).append(" Value: ").append(valueToString(objectInfo.value())).append('\n');

		builder.append("\tVariables:\n");
		for(VariableInfo f : VmUtil.collectAllFields(vm, classInfo))
			if(f.hasModifier(Modifier.STATIC))
				builder.append("\t\t").append(f.getFqName().shortName()).append(": ").append(f.getStaticValue()).append("\n");
			else
				builder.append("\t\t").append(f.getFqName().shortName()).append(": ").append(objectInfo.getVarValue(f)).append('\n');
		return builder.toString();
	}

	private static String valueToString(Object o)
	{
		if(o == null)
			return "null";
		if(o.getClass().isArray())
		{
			StringBuilder builder = new StringBuilder();
			builder.append("[{");

			int size = Array.getLength(o);
			for(int i = 0; i < size; i++)
			{
				Object val = Array.get(o, i);

				builder.append(valueToString(val));
				if(i != (size - 1))
					builder.append(", ");
			}
			builder.append("} : ").append(o.getClass().getName()).append("]");
			return builder.toString();
		}
		else
			return String.valueOf("[" + o + " : " + o.getClass().getName() + "]");
	}

	public static String toString(byte[] data, int len)
	{
		StringBuilder result = new StringBuilder();

		int counter = 0;

		for (int i = 0; i < len; i++)
		{
			if (counter % 16 == 0)
			{
				result.append(fillHex(i, 4)).append(": ");
			}

			result.append(fillHex(data[i] & 0xff, 2)).append(" ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");

				int charpoint = i - 15;
				for (int a = 0; a < 16; a++)
				{
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char) t1);
					}
					else
					{
						result.append('.');
					}
				}

				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0)
		{
			for (int i = 0; i < 17 - rest; i++)
			{
				result.append("   ");
			}

			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++)
			{
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char) t1);
				}
				else
				{
					result.append('.');
				}
			}

			result.append("\n");
		}


		return result.toString();
	}

	public static String fillHex(int data, int digits)
	{
		String number = Integer.toHexString(data);

		for (int i = number.length(); i < digits; i++)
			number = "0" + number;

		return number;
	}
}
