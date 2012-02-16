package org.napile.vm.invoke.impl.nativeimpl;

import java.lang.reflect.Method;

/**
 * @author VISTALL
 * @date 0:27/17.02.2012
 */
public class NativeMethod
{
	private String _name;
	private String[] _parameters;
	private Method _method;

	public NativeMethod(String name, String[] params, Method method)
	{
		_name = name;
		_parameters = params;
		_method = method;
	}

	public String getName()
	{
		return _name;
	}

	public String[] getParameters()
	{
		return _parameters;
	}

	public Method getMethod()
	{
		return _method;
	}
}
