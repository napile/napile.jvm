package org.napile.jvm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.jvm.bytecode.Instruction;
import org.napile.jvm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class lookupswitch implements Instruction
{
	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		buffer.getInt();
		int result = 0;
		
		for(int i = 0; i < 4; i++)
		{
			int thisByte = buffer.get(buffer.position()) & 0xFF;
			result += (int) (Math.pow(256, 3 - i)) * thisByte;
		}

		if(result < 0)
			result += 256;

		buffer.getInt();
	}

	@Override
	public void call(VmInterface vmInterface)
	{

	}
}
