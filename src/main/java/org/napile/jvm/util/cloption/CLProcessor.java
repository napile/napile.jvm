package org.napile.jvm.util.cloption;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.napile.jvm.util.ExitUtil;
import org.napile.jvm.vm.VmInterface;

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

	public void process(VmInterface vmInterface)
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
					ExitUtil.exitAbnormal(null, "Not find value for option: " + lastOption.getOptionName());
					return;
				}

				lastOption = option;
			}
			else
			{
				CLOptionProcessor processor = (lastOption != null ? lastOption : CLOption.MAIN_CLASS).getOptionProcessor();

				processor.process(vmInterface, value);

				lastOption = null;
			}
		}

		if(lastOption != null)
			ExitUtil.exitAbnormal(null, "Not find value for option: " + lastOption.getOptionName());
	}
}
