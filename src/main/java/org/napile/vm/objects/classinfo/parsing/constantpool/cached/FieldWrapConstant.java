package org.napile.vm.objects.classinfo.parsing.constantpool.cached;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.parsing.ClassParser;
import org.napile.vm.objects.classinfo.parsing.constantpool.Constant;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.ShortShortConstant;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.Utf8ValueConstant;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 21:21/15.02.2012
 */
public class FieldWrapConstant extends Constant
{
	private final ShortShortConstant _fieldRefConstant;
	private final ConstantPool _constantPool;

	private boolean _init;
	private FieldInfo _fieldInfo;

	public FieldWrapConstant(ConstantPool constantPool, ShortShortConstant shortShortConstant)
	{
		_constantPool = constantPool;
		_fieldRefConstant = shortShortConstant;
	}

	public FieldInfo getFieldInfo(Vm vm)
	{
		if(!_init)
		{
			ClassInfo classInfo = vm.getClass(ClassParser.getClassName(_constantPool, _fieldRefConstant.getFirstShort()));

			ShortShortConstant fieldInfoConstant = (ShortShortConstant)_constantPool.getConstant(_fieldRefConstant.getSecondShort());

			String name = ((Utf8ValueConstant)_constantPool.getConstant(fieldInfoConstant.getFirstShort())).getValue();

			FieldInfo fieldInfo = vm.getField(classInfo, name, true);
			if(fieldInfo == null)
				fieldInfo = vm.getStaticField(classInfo, name, true);

			_init = true;

			_fieldInfo = fieldInfo;
		}

		return _fieldInfo;
	}

	@Override
	public String toString()
	{
		return "FieldWrapConstant";
	}
}
