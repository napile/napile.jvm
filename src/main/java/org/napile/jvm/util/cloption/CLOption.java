package org.napile.jvm.util.cloption;

import org.napile.jvm.vm.VmContext;

/**
 * @author VISTALL
 * @date 16:22/31.01.2012
 */
public enum CLOption
{
	MAIN_CLASS("-main-class", new CLOptionProcessor()
	{
		@Override
		public void process(VmContext context, String value)
		{
			context.setMainClass(value);
		}
	}),

	CLASSPATH("-cp", new CLOptionProcessor()
	{
		@Override
		public void process(VmContext context, String value)
		{
			context.initClassPath(value);
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
