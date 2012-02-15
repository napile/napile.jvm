package org.napile.vm.objects.classinfo.parsing.constantpool.cached;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.parsing.constantpool.Constant;

/**
 * @author VISTALL
 * @date 21:21/15.02.2012
 */
public class FieldConstant extends Constant
{
	private ClassInfo _classInfo;
	private ClassInfo _typeClassInfo;
	private String _name;

	public FieldConstant(ClassInfo classInfo, ClassInfo typeClassInfo, String name)
	{
		_classInfo = classInfo;
		_typeClassInfo = typeClassInfo;
		_name = name;
	}

	public ClassInfo getClassInfo()
	{
		return _classInfo;
	}

	public String getName()
	{
		return _name;
	}

	@Override
	public String toString()
	{
		return _classInfo.getName() + "." + _name;
	}

	public ClassInfo getTypeClassInfo()
	{
		return _typeClassInfo;
	}
}
