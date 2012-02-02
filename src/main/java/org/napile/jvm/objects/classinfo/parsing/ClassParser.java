package org.napile.jvm.objects.classinfo.parsing;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.impl.ClassInfoImpl;
import org.napile.jvm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.jvm.objects.classinfo.parsing.constantpool.value.*;
import org.napile.jvm.util.ExitUtil;
import org.napile.jvm.vm.VmContext;
import org.napile.jvm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 16:02/31.01.2012
 */
public class ClassParser
{
	private static final Logger LOGGER = Logger.getLogger(ClassParser.class);

	private final DataInputStream _dataInputStream;
	private String _name;

	public ClassParser(InputStream stream, String name)
	{
		_dataInputStream = new DataInputStream(stream);
		_name = name;
	}

	public ClassInfo parse(VmContext context) throws IOException
	{
		int magic = _dataInputStream.readInt();
		if(magic != ClassInfo.MAGIC_HEADER)
		{
			ExitUtil.exitAbnormal("Invalid header of file. File: " + _name);
			return null;
		}

		int minorVersion = _dataInputStream.readUnsignedShort();
		int majorVersion = _dataInputStream.readUnsignedShort();
		if(!VmUtil.isSupported(majorVersion, minorVersion))
		{
			ExitUtil.exitAbnormal("Not supported file: " + majorVersion + "." + minorVersion + ". File: " + _name);
			return null;
		}

		int constantPoolSize = _dataInputStream.readUnsignedShort();
		ConstantPool constantPool = new ConstantPool(constantPoolSize);
		constantPool.addConstantPool(0, NothingConstant.STATIC);
		//int a = 1;
		for(int i = 1; i < constantPoolSize; i++)
		{
			byte type = _dataInputStream.readByte();
			Constant constant = null;
			switch(type)
			{
				case ConstantPool.CP_UTF8:
					constant = new Utf8ValueConstant(_dataInputStream.readUTF());
					break;
				case ConstantPool.CP_INTEGER:
					constant = new IntegerValueConstant(_dataInputStream.readInt());
					break;
				case ConstantPool.CP_FLOAT:
					constant = new FloatValueConstant(_dataInputStream.readFloat());
					break;
				case ConstantPool.CP_LONG:
					constant = new LongValueConstant(_dataInputStream.readLong());
					i++;
					break;
				case ConstantPool.CP_DOUBLE:
					constant = new DoubleValueConstant(_dataInputStream.readDouble());
					i++;
					break;
				case ConstantPool.CP_STRING:
				case ConstantPool.CP_CLASS:
					constant = new ShortValueConstant(_dataInputStream.readShort());
					break;
				case ConstantPool.CP_FIELD_DEF:
				case ConstantPool.CP_METHOD_DEF:
				case ConstantPool.CP_INTERFACE_DEF:
				case ConstantPool.CP_NAME:
					constant = new ShortShortConstant(_dataInputStream.readShort(), _dataInputStream.readShort());
					break;
				default:
					ExitUtil.exitAbnormal("Unknown constant pool type: " + type + ". File: " + _name);
					break;
			}

			constantPool.addConstantPool(i, constant);

			//System.out.println(constant.toString() + ">" + (a ++));
		}

		final short access = _dataInputStream.readShort();
		final String className = getClassName(constantPool, _dataInputStream.readShort());
		final String superClassName = getClassName(constantPool, _dataInputStream.readShort());
		ClassInfoImpl classInfo = new ClassInfoImpl(className, access);
		if(superClassName != null)
		{
			ClassInfo superClass = context.getClassInfoOrParse(superClassName);
			if(superClass == null)
			{
				ExitUtil.exitAbnormal("class.s1.not.found", superClassName);
				return null;
			}
			classInfo.setSuperClass(superClass);
		}

		final short interfacesSize = _dataInputStream.readShort();
		ClassInfo[] interfaces = new ClassInfo[interfacesSize];
		for(int i = 0; i < interfacesSize; i++)
		{
			String interfaceName = getClassName(constantPool, _dataInputStream.readShort());
			ClassInfo interfaceClass = context.getClassInfoOrParse(interfaceName);
			if(interfaceClass == null)
			{
				ExitUtil.exitAbnormal("class.s1.not.found", interfaceName);
				return null;
			}
			interfaces[i] = interfaceClass;
		}
		classInfo.setInterfaces(interfaces);

		return classInfo;
	}

	private static String getClassName(ConstantPool constantPool, int index)
	{
		if(index == 0)
			return null;
		ShortValueConstant shortValueConstant = (ShortValueConstant)constantPool.getConstant(index);

		Utf8ValueConstant utf8ValueConstant = (Utf8ValueConstant)constantPool.getConstant(shortValueConstant.getValue());

		return utf8ValueConstant.getValue().replace("/", ".");
	}
}
