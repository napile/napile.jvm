package org.napile.jvm.bytecode.impl;

import java.nio.ByteBuffer;

import org.napile.jvm.bytecode.Instruction;
import org.napile.jvm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class tableswitch implements Instruction
{
	private int[][] _data;
	private int _offset;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		int align = ((buffer.position() + 3) & 0x7ffffffc) - buffer.position();
		buffer.position(buffer.position() + align);

		_offset = buffer.getInt();
		int low = buffer.getInt();
		int high = buffer.getInt();

		_data = new int[high - low + 1][];
		for(int i = low; i <= high; i++)
		{
			int[] casePart = new int[2];
			casePart[0] = i;
			casePart[1] = buffer.getInt();

			_data[i - low] = casePart;
		}
	}

	@Override
	public void call(VmInterface vmInterface)
	{

	}
}
