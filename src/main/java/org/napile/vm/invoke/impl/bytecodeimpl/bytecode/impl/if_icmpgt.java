package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import gnu.trove.map.TIntIntMap;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.objects.objectinfo.impl.primitive.IntObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class if_icmpgt extends Instruction
{
	private int _index, _positionOnFalse;

	// temp
	private boolean _temp;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		_index = buffer.getShort() + getArrayIndex();
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		IntObjectInfo objectInfo1 = (IntObjectInfo)context.last();
		IntObjectInfo objectInfo2 = (IntObjectInfo)context.last();

		_temp = objectInfo1.getValue() <= objectInfo2.getValue();
	}

	@Override
	public int getNextIndex(int index)
	{
		return _temp ? index + 1 : _positionOnFalse;
	}

	@Override
	public void findIndexes(TIntIntMap map)
	{
		_positionOnFalse = map.get(_index);
	}

	@Override
	public String toString()
	{
		return super.toString() + " [a" + _index + ", i" + _positionOnFalse + "]";
	}
}
