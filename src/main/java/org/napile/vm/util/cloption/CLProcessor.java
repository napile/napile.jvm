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

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.napile.vm.util.BundleUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 16:24/31.01.2012
 */
public class CLProcessor
{
	private Deque<String> _deque;

	public CLProcessor(String[] arguments)
	{
		_deque = new ArrayDeque<String>(Arrays.asList(arguments));
	}

	public void process(Vm vm)
	{
		CLOption lastOption = null;

		String value = null;
		while((value = _deque.pollFirst()) != null)
		{
			CLOption option = CLOption.parse(value);
			if(option != null)
			{
				if(lastOption != null)
				{
					BundleUtil.exitAbnormal(null, "Not find value for option: " + lastOption.getOptionName());
					return;
				}

				lastOption = option;
			}
			else
			{
				CLOptionProcessor processor = (lastOption != null ? lastOption : CLOption.MAIN_CLASS).getOptionProcessor();

				processor.process(vm, value);

				lastOption = null;
			}
		}

		if(lastOption != null)
			BundleUtil.exitAbnormal(null, "Not find value for option: " + lastOption.getOptionName());
	}
}
