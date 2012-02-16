package org.napile.vm.objects.classinfo.parsing.constantpool.cached;

import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.parsing.ClassParser;
import org.napile.vm.objects.classinfo.parsing.constantpool.Constant;
import org.napile.vm.objects.classinfo.parsing.constantpool.ConstantPool;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.ShortShortConstant;
import org.napile.vm.objects.classinfo.parsing.constantpool.binary.Utf8ValueConstant;
import org.napile.vm.vm.VmInterface;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 0:50/16.02.2012
 */
public class MethodRefConstant extends Constant
{
	private final ShortShortConstant _methodRefConstant;
	private final ConstantPool _constantPool;

	private boolean _init;
	private MethodInfo _methodInfo;

	public MethodRefConstant(ConstantPool constantPool, ShortShortConstant shortShortConstant)
	{
		_constantPool = constantPool;
		_methodRefConstant = shortShortConstant;
	}

	public MethodInfo getMethodInfo(VmInterface vmInterface)
	{
		if(!_init)
		{
			String className = ClassParser.getClassName(_constantPool, _methodRefConstant.getFirstShort());
			ClassInfo classInfo = vmInterface.getClass(ClassParser.getClassName(_constantPool, _methodRefConstant.getFirstShort()));
			if(classInfo == null && className.charAt(0) == '[')
				classInfo = VmUtil.parseType(vmInterface, className);

			ShortShortConstant methodInfoConstant = (ShortShortConstant)_constantPool.getConstant(_methodRefConstant.getSecondShort());

			String name = ((Utf8ValueConstant)_constantPool.getConstant(methodInfoConstant.getFirstShort())).getValue();
			String desc = ((Utf8ValueConstant)_constantPool.getConstant(methodInfoConstant.getSecondShort())).getValue();

			//ClassInfo returnType = VmUtil.parseType(vmInterface, desc.substring(desc.indexOf(')') + 1, desc.length()));
			ClassInfo[] parameters = ClassParser.parseMethodSignature(vmInterface, desc.substring(1, desc.indexOf(')')));
			String[] stringParams = new String[parameters.length];
			for(int j = 0; j < parameters.length; j++)
				stringParams[j] = parameters[j].getName();

			MethodInfo methodInfo = vmInterface.getMethod(classInfo, name, true, stringParams);
			if(methodInfo == null)
				methodInfo = vmInterface.getStaticMethod(classInfo, name, true, stringParams);

			_methodInfo = methodInfo;
			_init = true;
		}
		return _methodInfo;
	}

	@Override
	public String toString()
	{
		return "MethodRefConstant";
	}
}
