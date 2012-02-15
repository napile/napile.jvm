package org.napile.vm.util.cloption;

import org.napile.vm.util.ClasspathUtil;
import org.napile.vm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 16:22/31.01.2012
 */
public enum CLOption
{
	MAIN_CLASS("-main-class", new CLOptionProcessor()
	{
		@Override
		public void process(VmInterface vmInterface, String value)
		{
			vmInterface.getVmContext().setMainClass(value);
		}
	}),

	CLASSPATH("-cp", new CLOptionProcessor()
	{
		@Override
		public void process(VmInterface vmInterface, String value)
		{
			ClasspathUtil.initClassPath(vmInterface, value);
		}
	});

	private static final CLOption[] VALUES = values();
	private CLOptionProcessor _optionProcessor;
	private String _name;

	CLOption(String name, CLOptionProcessor processor)
	{
		_name = name;
		_optionProcessor = processor;
	}

	public static CLOption parse(String val)
	{
		for(CLOption option : VALUES)
			if(option._name.equals(val))
				return option;

		return null;
	}

	public String getOptionName()
	{
		return _name;
	}

	public CLOptionProcessor getOptionProcessor()
	{
		return _optionProcessor;
	}
}
