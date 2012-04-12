package org.napile.vm.objects.objectinfo.impl;

import org.napile.vm.objects.classinfo.ClassInfo;

/**
 * @author VISTALL
 * @date 23:14/15.02.2012
 */
public class ArrayObjectInfo extends ValueObjectInfo<Object[]>
{
	public ArrayObjectInfo(ClassInfo classInfo, Object[] value)
	{
		super(classInfo, value);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for(Object o : getValue())
			if(o != null)
				builder.append(o.toString());
		builder.append("}");

		return builder.toString();
	}
}
