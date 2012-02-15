package org.napile.vm.objects.classinfo.parsing.constantpool;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.parsing.ClassParser;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.ShortShortConstant;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.Utf8ValueConstant;
import org.napile.vm.objects.classinfo.parsing.constantpool.cached.FieldConstant;
import org.napile.vm.util.ClasspathUtil;
import org.napile.vm.util.StringCharReader;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 16:08/31.01.2012
 */
public class ConstantPool
{
	public static final int CP_NOTHING = -1;
	public static final int CP_UTF8 = 1;
	public static final int CP_INTEGER = 3;
	public static final int CP_FLOAT = 4;
	public static final int CP_LONG = 5;
	public static final int CP_DOUBLE = 6;
	public static final int CP_CLASS = 7;
	public static final int CP_STRING = 8;
	public static final int CP_FIELD_DEF = 9;
	public static final int CP_METHOD_DEF = 10;
	public static final int CP_INTERFACE_DEF = 11;
	public static final int CP_NAME = 12;

	private final Constant[] _constants;

	public ConstantPool(int size)
	{
		_constants = new Constant[size];
	}

	public void addConstantPool(int index, Constant value)
	{
		_constants[index] = value;
	}

	public Constant getConstant(int val)
	{
		return _constants[val];
	}

	public void makeCached(VmInterface vmInterface)
	{
		for(int i = 0; i < _constants.length; i++)
		{
			Constant constant = _constants[i];
			if(constant == null)
				continue;
			switch(constant.getType())
			{
				case CP_FIELD_DEF:
					ShortShortConstant shortShortConstant = (ShortShortConstant)constant;

					ClassInfo classInfo = ClasspathUtil.getClassInfoOrParse(vmInterface, ClassParser.getClassName(this, shortShortConstant.getFirstShort()));
					ShortShortConstant fieldInfoConstant = (ShortShortConstant)getConstant(shortShortConstant.getSecondShort());

					String name = ((Utf8ValueConstant)getConstant(fieldInfoConstant.getFirstShort())).getValue();

					ClassInfo typeClassInfo = ClassParser.parseType(vmInterface, new StringCharReader(((Utf8ValueConstant) getConstant(fieldInfoConstant.getSecondShort())).getValue().replace("/", ".")));

					_constants[i] = new FieldConstant(classInfo, typeClassInfo, name);
					break;
			}
		}
	}
}
