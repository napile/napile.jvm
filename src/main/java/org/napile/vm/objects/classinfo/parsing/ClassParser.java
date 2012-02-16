package org.napile.vm.objects.classinfo.parsing;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.napile.vm.Main;
import org.napile.vm.invoke.impl.BytecodeInvokeType;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.InstructionFactory;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.FieldInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.ReflectInfo;
import org.napile.vm.objects.classinfo.impl.ClassInfoImpl;
import org.napile.vm.objects.classinfo.impl.FieldInfoImpl;
import org.napile.vm.objects.classinfo.impl.MethodInfoImpl;
import org.napile.vm.objects.classinfo.parsing.constantpool.Constant;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.classinfo.parsing.constantpool.ValueConstant;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.*;
import org.napile.vm.objects.classinfo.parsing.variabletable.LocalVariable;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.util.BundleUtil;
import org.napile.vm.util.ClasspathUtil;
import org.napile.vm.util.StringCharReader;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 16:02/31.01.2012
 */
public class ClassParser
{
	private static final Logger LOGGER = Logger.getLogger(ClassParser.class);

	private final DataInputStream _dataInputStream;
	private String _name;
	private Vm _vm;

	public ClassParser(Vm vm, InputStream stream, String name)
	{
		_vm = vm;
		_dataInputStream = new DataInputStream(stream);
		_name = name;
	}

	public String parseQuickName() throws IOException
	{
		int magic = _dataInputStream.readInt();
		if(magic != ClassInfo.MAGIC_HEADER)
		{
			BundleUtil.exitAbnormal(null, "Invalid header of file. File: " + _name);
			return null;
		}

		int minorVersion = _dataInputStream.readUnsignedShort();
		int majorVersion = _dataInputStream.readUnsignedShort();
		if(!Main.isSupported(majorVersion, minorVersion))
		{
			BundleUtil.exitAbnormal(null, "Not supported file: " + majorVersion + "." + minorVersion + ". File: " + _name);
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
			BundleUtil.exitAbnormal(null, "Invalid header of file. File: " + _name);
			return null;
		}

		int minorVersion = _dataInputStream.readUnsignedShort();
		int majorVersion = _dataInputStream.readUnsignedShort();
		if(!Main.isSupported(majorVersion, minorVersion))
		{
			BundleUtil.exitAbnormal(null, "Not supported file: " + majorVersion + "." + minorVersion + ". File: " + _name);
			return null;
		}

		ConstantPool constantPool = parseConstantPool();

		final short access = _dataInputStream.readShort();
		final String className = getClassName(constantPool, _dataInputStream.readShort());
		final String superClassName = getClassName(constantPool, _dataInputStream.readShort());
		ClassInfoImpl classInfo = new ClassInfoImpl(constantPool, className, access);
		_vm.getCurrentClassLoader().addClassInfo(classInfo); ///need add fist - for circle depends

		if(superClassName != null)
		{
			ClassInfo superClass = ClasspathUtil.getClassInfoOrParse(_vm, superClassName);
			if(superClass == null)
			{
				BundleUtil.exitAbnormal(null, "class.s1.not.found", superClassName);
				return null;
			}
			classInfo.setSuperClass(superClass);
		}

		final short interfacesSize = _dataInputStream.readShort();
		ClassInfo[] interfaces = new ClassInfo[interfacesSize];
		for(int i = 0; i < interfacesSize; i++)
		{
			String interfaceName = getClassName(constantPool, _dataInputStream.readShort());
			ClassInfo interfaceClass = ClasspathUtil.getClassInfoOrParse(_vm, interfaceName);
			if(interfaceClass == null)
			{
				BundleUtil.exitAbnormal(null, "class.s1.not.found", interfaceName);
				return null;
			}
			interfaces[i] = interfaceClass;
		}
		classInfo.setInterfaces(interfaces);

		parseFields(classInfo, constantPool);

		parseMethods(classInfo, constantPool);

		constantPool.makeCached();
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
					BundleUtil.exitAbnormal(null, "Unknown constant pool type: " + type + ". File: " + _name);
					break;
			}

			constant.setType(type);
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

			FieldInfoImpl fieldInfo = new FieldInfoImpl(classInfo, VmUtil.parseType(_vm, getSimpleUtf8Name(constantPool, descIndex)), getSimpleUtf8Name(constantPool, nameIndex), accessFlags);
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
						BundleUtil.exitAbnormal(null, "invalid.constant.value.class.s1", classInfo.getName());
					else
					{
						ValueConstant<?> valueConstant =  (ValueConstant) constant;
						if(valueConstant.getType() == ConstantPool.CP_STRING)
							valueConstant = (ValueConstant)constantPool.getConstant(((ShortValueConstant)valueConstant).getValue());

						fieldInfo.setTempValue(valueConstant.getValue());
					}
				}
				else if(attributeName.equals(ReflectInfo.ATT_SIGNATURE))
				{
					_dataInputStream.readInt(); // lenght ?mm
					short index = _dataInputStream.readShort();
					Constant constant = constantPool.getConstant(index);

					if(!(constant instanceof ValueConstant))
						BundleUtil.exitAbnormal(null, "invalid.attribute.class.s1", attributeName, classInfo.getName());
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
					BundleUtil.exitAbnormal(null, "invalid.attribute.class.s1", attributeName, classInfo.getName());
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

			ClassInfo returnType = VmUtil.parseType(_vm, desc.substring(desc.indexOf(')') + 1, desc.length()));

			ClassInfo[] parameters = parseMethodSignature(_vm, desc.substring(1, desc.indexOf(')')));

			MethodInfoImpl methodInfo = new MethodInfoImpl(classInfo, returnType, parameters, getSimpleUtf8Name(constantPool, nameIndex), accessFlags);
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
						BundleUtil.exitAbnormal(null, "invalid.attribute.class.s1", attributeName, classInfo.getName());
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
						throwsClassInfo[a] = ClasspathUtil.getClassInfoOrParse(_vm, getClassName(constantPool, _dataInputStream.readShort()));
					methodInfo.setThrowExceptions(throwsClassInfo);
				}
				else if(attributeName.equals(ReflectInfo.ATT_CODE))
				{
					AssertUtil.assertNotNull(methodInfo.getInvokeType());

					BytecodeInvokeType invokeType = new BytecodeInvokeType();
					methodInfo.setInvokeType(invokeType);

					_dataInputStream.readInt();

					invokeType.setMaxStack(_dataInputStream.readShort());
					invokeType.setMaxLocals(_dataInputStream.readShort());

					int codeLen = _dataInputStream.readInt();

					byte[] btArray = new byte[codeLen];
					_dataInputStream.readFully(btArray);

					Instruction[] instructions = InstructionFactory.parseByteCode(_name, methodInfo.getName(), btArray);
					invokeType.setInstructions(instructions);

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

					parseMethodCodeAttribute(invokeType, classInfo, methodInfo, constantPool);
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
					BundleUtil.exitAbnormal(null, "invalid.attribute.class.s1", attributeName, classInfo.getName());
			}
			//break;
		}

		classInfo.setMethods(methods);
	}

	private void parseMethodCodeAttribute(BytecodeInvokeType bytecodeInvokeType, ClassInfoImpl classInfo, MethodInfoImpl methodInfo, ConstantPool constantPool) throws IOException
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

				short variableCount = _dataInputStream.readShort();
				LocalVariable[] localVariables = new LocalVariable[variableCount];
				for(int i = 0; i < variableCount; i++)
				{
					short startPc = _dataInputStream.readShort();
					short length = _dataInputStream.readShort();
					short nameIndex = _dataInputStream.readShort();
					short descIndex = _dataInputStream.readShort();
					short frameIndex = _dataInputStream.readShort();

					String name = getSimpleUtf8Name(constantPool, nameIndex);
					String typeName = getSimpleUtf8Name(constantPool, descIndex);
					ClassInfo typeClassInfo = VmUtil.parseType(_vm, typeName);
					if(typeClassInfo == null)
					{
						BundleUtil.exitAbnormal(null, "class.s1.not.found", typeName);
						return;
					}

					LocalVariable variable = new LocalVariable(startPc, length, name, typeClassInfo, frameIndex);
					localVariables[i] = variable;
				}
				bytecodeInvokeType.setLocalVariables(localVariables);
			}
			else
				BundleUtil.exitAbnormal(null, "invalid.attribute.class.s1", codeAttributeName, classInfo.getName());
		}
	}

	public static ClassInfo[] parseMethodSignature(Vm vm, String sig)
	{
		List<ClassInfo> parameters = new ArrayList<ClassInfo>(2);
		StringCharReader reader = new StringCharReader(sig);
		while(reader.hasNext())
			parameters.add(VmUtil.parseType(vm, reader));

		return parameters.isEmpty() ? ClassInfo.EMPTY_ARRAY : parameters.toArray(new ClassInfo[parameters.size()]);
	}

	public static String getClassName(ConstantPool constantPool, int index)
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
