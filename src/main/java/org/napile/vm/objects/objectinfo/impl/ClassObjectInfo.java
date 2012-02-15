package org.napile.vm.objects.objectinfo.impl;

import java.util.HashMap;
import java.util.Map;

import org.napile.commons.pair.Pair;
import org.napile.vm.objects.Flags;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 23:25/15.02.2012
 */
public class ClassObjectInfo  extends ObjectInfo
{
	private Map<String, Pair<FieldInfo, ObjectInfo>> _fields = new HashMap<String, Pair<FieldInfo, ObjectInfo>>();
	private ClassInfo _classInfo;

	public ClassObjectInfo(ObjectInfo classObjectInfo, ClassInfo classInfo)
	{
		super(classObjectInfo);
		_classInfo = classInfo;

		ClassInfo current = classInfo;
		while(current != null)
		{
			for(FieldInfo f : current.getFields())
			{
				if(Flags.isStatic(f))
					continue;

				Pair<FieldInfo, ObjectInfo> pair = new Pair<FieldInfo, ObjectInfo>(f, f.getType().nullValue());

				_fields.put(current.getName() + "." + f.getName(), pair);
			}

			current = current.getSuperClass();
		}
	}
}
