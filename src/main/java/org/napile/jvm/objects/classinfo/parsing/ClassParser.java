package org.napile.jvm.objects.classinfo.parsing;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.napile.jvm.bytecode.Instruction;
import org.napile.jvm.bytecode.InstructionFactory;
import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.FieldInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;
import org.napile.jvm.objects.classinfo.ReflectInfo;
import org.napile.jvm.objects.classinfo.impl.ArrayClassInfoImpl;
import org.napile.jvm.objects.classinfo.impl.ClassInfoImpl;
import org.napile.jvm.objects.classinfo.impl.FieldInfoImpl;
import org.napile.jvm.objects.classinfo.impl.MethodInfoImpl;
import org.napile.jvm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.jvm.objects.classinfo.parsing.constantpool.value.*;
import org.napile.jvm.util.ExitUtil;
import org.napile.jvm.util.StringCharReader;
import org.napile.jvm.vm.VmContext;
import org.napile.jvm.vm.VmInterface;
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
	private VmContext _vmContext;

	public ClassParser(VmContext vmContext, InputStream stream, String name)
	{
		_vmContext = vmContext;
		_dataInputStream = new DataInputStream(stream);
		_name = name;
	}

	public String parseQuickName() throws IOException
	{
		int magic = _dataInputStream.readInt();
		if(magic != ClassInfo.MAGIC_HEADER)
		{
			ExitUtil.exitAbnormal(null, "Invalid header of file. File: " + _name);
			return null;
		}

		int minorVersion = _dataInputStream.readUnsignedShort();
		int majorVersion = _dataInputStream.readUnsignedShort();
		if(!VmUtil.isSupported(majorVersion, minorVersion))
		{
			ExitUtil.exitAbnormal(null, "Not supported file: " + majorVersion + "." + minorVersion + ". File: " + _name);
			return null;
		}

		ConstantPool constantPool = parseConstantPool();

		_dataInputStream.readShort();
		final String className = getClassName(constantPool, _dataInputStream.readShort());

		return className.replace("/", ".");
	}

	public ClassInfo parse() throws IOException
	{
		int magic = _dataInputStream.readInt();
		if(magic != ClassInfo.MAGIC_HEADER)
		{
			ExitUtil.exitAbnormal(null, "Invalid header of file. File: " + _name);
			return null;
		}

		int minorVersion = _dataInputStream.readUnsignedShort();
		int majorVersion = _dataInputStream.readUnsignedShort();
		if(!VmUtil.isSupported(majorVersion, minorVersion))
		{
			ExitUtil.exitAbnormal(null, "Not supported file: " + majorVersion + "." + minorVersion + ". File: " + _name);
			return null;
		}

		ConstantPool constantPool = parseConstantPool();

		final short access = _dataInputStream.readShort();
		final String className = getClassName(constantPool, _dataInputStream.readShort());
		final String superClassName = getClassName(constantPool, _dataInputStream.readShort());
		ClassInfoImpl classInfo = new ClassInfoImpl(className, access);
		_vmContext.addClassInfo(classInfo); ///need add fist - for circle depends
		if(superClassName != null)
		{
			ClassInfo superClass = _vmContext.getClassInfoOrParse(superClassName);
			if(superClass == null)
			{
				ExitUtil.exitAbnormal(null, "class.s1.not.found", superClassName);
				return null;
			}
			classInfo.setSuperClass(superClass);
		}

		final short interfacesSize = _dataInputStream.readShort();
		ClassInfo[] interfaces = new ClassInfo[interfacesSize];
		for(int i = 0; i < interfacesSize; i++)
		{
			String interfaceName = getClassName(constantPool, _dataInputStream.readShort());
			ClassInfo interfaceClass = _vmContext.getClassInfoOrParse(interfaceName);
			if(interfaceClass == null)
			{
				ExitUtil.exitAbnormal(null, "class.s1.not.found", interfaceName);
				return null;
			}
			interfaces[i] = interfaceClass;
		}
		classInfo.setInterfaces(interfaces);

		parseFields(classInfo, constantPool);

		parseMethods(classInfo, constantPool);

		return classInfo;
	}

	private ConstantPool parseConstantPool() throws IOException
	{
		int constantPoolSize = _dataInputStream.readUnsignedShort();
		ConstantPool constantPool = new ConstantPool(constantPoolSize);

		constantPool.addConstantPool(0, NothingConstant.STATIC);

		for(int i = 1; i < constantPoolSize; i++)
		{
			final int indexToPut = i;
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
					ExitUtil.exitAbnormal(null, "Unknown constant pool type: " + type + ". File: " + _name);
					break;
			}

			constantPool.addConstantPool(indexToPut, constant);
		}

		return constantPool;
	}

	private void parseFields(ClassInfoImpl classInfo, ConstantPool constantPool) throws IOException
	{
		final short fieldsSize = _dataInputStream.readShort();
		FieldInfo[] fields = new FieldInfo[fieldsSize];
		for(int i = 0; i < fields.length; i++)
		{
			short accessFlags = _dataInputStream.readShort();
			short nameIndex = _dataInputStream.readShort();
			short descIndex = _dataInputStream.readShort();

			FieldInfoImpl fieldInfo = new FieldInfoImpl(parseType(_vmContext, new StringCharReader(getSimpleUtf8Name(constantPool, descIndex))), getSimpleUtf8Name(constantPool, nameIndex), accessFlags);
			fields[i] = fieldInfo;
			short attributeSize = _dataInputStream.readShort();
			for(int j = 0; j < attributeSize; j++)
			{
				String attributeName = getSimpleUtf8Name(constantPool, _dataInputStream.readShort());
				if(attributeName.equals(ReflectInfo.ATT_CONSTANT_VALUE))
				{
					_dataInputStream.readInt(); // lenght ?mm
					short index = _dataInputStream.readShort();
					Constant constant = constantPool.getConstant(index);
					if(!(constant instanceof ValueConstant))
						ExitUtil.exitAbnormal(null, "invalid.constant.value.class.s1", classInfo.getName());
					else
						fieldInfo.setValue(((ValueConstant) constant).getValue());
				}
				else if(attributeName.equals(ReflectInfo.ATT_SIGNATURE))
				{
					_dataInputStream.readInt(); // lenght ?mm
					short index = _dataInputStream.readShort();
					Constant constant = constantPool.getConstant(index);

					if(!(constant instanceof ValueConstant))
						ExitUtil.exitAbnormal(null, "invalid.attribute.class.s1", attributeName, classInfo.getName());
					else
					{
						//TODO [VISTALL] make it
					}
				}
				else if(attributeName.equals(ReflectInfo.ATT_DEPRECATED))
				{
					_dataInputStream.readInt(); // must be zero?
				}
				else if(attributeName.equals(ReflectInfo.ATT_RUNTIME_VISIBLE_ANNOTATIONS))
				{
					//TODO [VISTALL]
					_dataInputStream.readFully(new byte[_dataInputStream.readInt()]);
				}
				else
					ExitUtil.exitAbnormal(null, "invalid.attribute.class.s1", attributeName, classInfo.getName());
			}
		}

		classInfo.setFields(fields);
	}

	private void parseMethods(ClassInfoImpl classInfo, ConstantPool constantPool) throws IOException
	{
		final short methodSize = _dataInputStream.readShort();
		MethodInfo[] methods = new MethodInfo[methodSize];
		for(int i = 0; i < methods.length; i++)
		{
			short accessFlags = _dataInputStream.readShort();
			short nameIndex = _dataInputStream.readShort();
			short descIndex = _dataInputStream.readShort();
			//()V
			String desc = getSimpleUtf8Name(constantPool, descIndex);

			ClassInfo returnType = parseType(_vmContext, new StringCharReader(desc.substring(desc.indexOf(')') + 1, desc.length())));

			ClassInfo[] parameters = parseMethodSignature(_vmContext, desc.substring(1, desc.indexOf(')')));

			MethodInfoImpl methodInfo = new MethodInfoImpl(returnType, parameters, getSimpleUtf8Name(constantPool, nameIndex), accessFlags);
			methods[i] = methodInfo;
			short attributeSize = _dataInputStream.readShort();
			for(int j = 0; j < attributeSize; j++)
			{
				String attributeName = getSimpleUtf8Name(constantPool, _dataInputStream.readShort());
				if(attributeName.equals(ReflectInfo.ATT_SIGNATURE))
				{
					_dataInputStream.readInt(); // lenght ?mm
					short index = _dataInputStream.readShort();
					Constant constant = constantPool.getConstant(index);

					if(!(constant instanceof ValueConstant))
						ExitUtil.exitAbnormal(null, "invalid.attribute.class.s1", attributeName, classInfo.getName());
					else
					{
						//TODO [VISTALL] make it
					}
				}
				else if(attributeName.equals(ReflectInfo.ATT_EXCEPTIONS))
				{
					_dataInputStream.readInt();
					short numException = _dataInputStream.readShort();
					ClassInfo[] throwsClassInfo = new ClassInfo[numException];
					for(int a = 0; a < numException; a++)
						throwsClassInfo[a] = _vmContext.getClassInfoOrParse(getClassName(constantPool, _dataInputStream.readShort()));
					methodInfo.setThrowExceptions(throwsClassInfo);
				}
				else if(attributeName.equals(ReflectInfo.ATT_CODE))
				{
					_dataInputStream.readInt();

					short maxStack = _dataInputStream.readShort();
					//aLocalMethod.setMaxStack(maxStack);

					short maxLocals = _dataInputStream.readShort();
					//aLocalMethod.setMaxLocals(maxLocals);

					int codeLen = _dataInputStream.readInt();

					byte[] btArray = new byte[codeLen];
					_dataInputStream.readFully(btArray);
					Instruction[] instructions = InstructionFactory.parseByteCode(_name, btArray);
					//aLocalMethod.setBytes(btArray);

					// exception_table_length
					short excLen = _dataInputStream.readShort();

					/**
					 * startPc - Offset of start of try/catch range. endPc - Offset of end of
					 * try/catch range. handlerPc - Offset of start of exception handler code.
					 * catchType - Type of exception handled.
					 */
					for(int a = 0; a < excLen; a++)
					{
						short startPc = _dataInputStream.readShort();
						short endPc = _dataInputStream.readShort();
						short handlerPc = _dataInputStream.readShort();
						short catchType = _dataInputStream.readShort();

						// If type of class caught is any, then CatchType is 0.
						//aLocalMethod.addExceptionBlock(startPc, endPc, handlerPc, aCpInfo.getClassName(catchType));
					}

					parseMethodCodeAttribute(classInfo, methodInfo, constantPool);
				}
				else if(attributeName.equals(ReflectInfo.ATT_RUNTIME_VISIBLE_ANNOTATIONS))
				{
					//TODO [VISTALL]
					_dataInputStream.readFully(new byte[_dataInputStream.readInt()]);
				}
				else if(attributeName.equals(ReflectInfo.ATT_DEPRECATED))
				{
					_dataInputStream.readInt(); // must be zero?
				}
				else
					ExitUtil.exitAbnormal(null, "invalid.attribute.class.s1", attributeName, classInfo.getName());
			}
			//break;
		}

		classInfo.setMethods(methods);
	}

	private void parseMethodCodeAttribute(ClassInfoImpl classInfo, MethodInfoImpl methodInfo, ConstantPool constantPool) throws IOException
	{
		short attrCount = _dataInputStream.readShort();

		for(int a = 0; a < attrCount; a++)
		{
			String codeAttributeName = getSimpleUtf8Name(constantPool, _dataInputStream.readShort());
			if(codeAttributeName.equals(ReflectInfo.ATT_LINE_NUMBER_TABLE))
			{
				//TODO [VISTALL] 
				_dataInputStream.readFully(new byte[_dataInputStream.readInt()]);
			}
			else if(codeAttributeName.equals(ReflectInfo.ATT_STACK_MAP_TABLE))
			{
				//TODO [VISTALL]
				_dataInputStream.readFully(new byte[_dataInputStream.readInt()]);
			}
			else if(codeAttributeName.equals(ReflectInfo.ATT_LOCAL_VARIABLE_TABLE))
			{
				_dataInputStream.readInt(); //len

				short localVarArrLen = _dataInputStream.readShort();
				for(int ctr = 1; ctr <= localVarArrLen; ctr++)
				{
					short startPc = _dataInputStream.readShort();
					short length = _dataInputStream.readShort();
					short nameIndex = _dataInputStream.readShort();
					short descIndex = _dataInputStream.readShort();
					short frameIndex = _dataInputStream.readShort();
				}
			}
			else
				ExitUtil.exitAbnormal(null, "invalid.attribute.class.s1", codeAttributeName, classInfo.getName());
		}
	}

	private static ClassInfo[] parseMethodSignature(VmContext vmContext, String sig)
	{
		List<ClassInfo> parameters = new ArrayList<ClassInfo>(2);
		StringCharReader reader = new StringCharReader(sig);
		while(reader.hasNext())
			parameters.add(parseType(vmContext, reader));

		return parameters.isEmpty() ? ClassInfo.EMPTY_ARRAY : parameters.toArray(new ClassInfo[parameters.size()]);
	}

	private static ClassInfo parseType(VmContext vmContext, StringCharReader charReader)
	{
		char firstChar = charReader.next();
		switch(firstChar)
		{
			case '[':  //array
				int i = 1;//array size
				while(charReader.next() == firstChar)
					i++;

				charReader.back(); //need go back after while

				ClassInfo arrayTypeInfo = parseType(vmContext, charReader);
				if(arrayTypeInfo == null)
				{
					ExitUtil.exitAbnormal(null, "class.s1.not.found", charReader);
					return null;
				}

				ClassInfo arrayClass = new ArrayClassInfoImpl(arrayTypeInfo);
				if(i > 1)
					for(int a = 1; a < i; a++)
						arrayClass = new ArrayClassInfoImpl(arrayClass);

				ClassInfo storedClassInfo = vmContext.getClass(arrayClass.getName());
				if(storedClassInfo == null)
					vmContext.addClassInfo(arrayClass);
				else
					arrayClass = storedClassInfo;
				return arrayClass;
			case 'J': //long
				return vmContext.getClassInfoOrParse(VmInterface.PRIMITIVE_LONG);
			case 'C':  //char
				return vmContext.getClassInfoOrParse(VmInterface.PRIMITIVE_CHAR);
			case 'B':  //byte
				return vmContext.getClassInfoOrParse(VmInterface.PRIMITIVE_BYTE);
			case 'D':  //double
				return vmContext.getClassInfoOrParse(VmInterface.PRIMITIVE_DOUBLE);
			case 'F':  //float
				return vmContext.getClassInfoOrParse(VmInterface.PRIMITIVE_FLOAT);
			case 'I':  //int
				return vmContext.getClassInfoOrParse(VmInterface.PRIMITIVE_INT);
			case 'S':  //short
				return vmContext.getClassInfoOrParse(VmInterface.PRIMITIVE_SHORT);
			case 'Z':  //boolean
				return vmContext.getClassInfoOrParse(VmInterface.PRIMITIVE_BOOLEAN);
			case 'V':  //void
				return vmContext.getClassInfoOrParse(VmInterface.PRIMITIVE_VOID);
			case 'T': //generic
				//TODO [VISTALL] make it
				return vmContext.getClassInfoOrParse("java.lang.Object");
			case 'L': //class
				StringBuilder b = new StringBuilder();
				while(charReader.next() != ';')
					b.append(charReader.current());

				String text = b.toString().replace("/", ".");
				ClassInfo classInfo = vmContext.getClassInfoOrParse(text);
				if(classInfo == null)
				{
					ExitUtil.exitAbnormal(null, "class.s1.not.found", text);
					return null;
				}
				else
					return classInfo;
			default:
				LOGGER.error("unknown type: " + firstChar);
		}
		return null;
	}

	private static String getClassName(ConstantPool constantPool, int index)
	{
		if(index == 0)
			return null;
		ShortValueConstant shortValueConstant = (ShortValueConstant) constantPool.getConstant(index);

		Utf8ValueConstant utf8ValueConstant = (Utf8ValueConstant) constantPool.getConstant(shortValueConstant.getValue());

		return utf8ValueConstant.getValue().replace("/", ".");
	}

	public static String getSimpleUtf8Name(ConstantPool constantPool, short index)
	{
		Utf8ValueConstant utf8ValueConstant = (Utf8ValueConstant) constantPool.getConstant(index);

		return utf8ValueConstant.getValue();
	}
}
