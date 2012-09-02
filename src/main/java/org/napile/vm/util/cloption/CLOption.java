/*
 * Copyright 2010-2012 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.vm.util.cloption;

import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.util.ClasspathUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 16:22/31.01.2012
 */
public enum CLOption
{
	ARGS("-args", new CLOptionProcessor()
	{
		@Override
		public void process(Vm vm, String value)
		{
			vm.getVmContext().getArguments().add(value);
		}
	}),

	MAIN_CLASS("-main-class", new CLOptionProcessor()
	{
		@Override
		public void process(Vm vm, String value)
		{
			if(vm.getVmContext().getMainClass() != null)
			{
				ARGS.getOptionProcessor().process(vm, value);

				return;
			}

			vm.getVmContext().setMainClass(new FqName(value));
		}
	}),

	CLASSPATH("-cp", new CLOptionProcessor()
	{
		@Override
		public void process(Vm vm, String value)
		{
			ClasspathUtil.initClassPath(vm, value);
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
