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
