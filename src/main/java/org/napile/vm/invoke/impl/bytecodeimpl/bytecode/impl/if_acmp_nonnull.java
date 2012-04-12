package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import gnu.trove.map.TIntIntMap;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class if_acmp_nonnull extends Instruction
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
		ObjectInfo objectInfo = context.last();
		_temp = objectInfo.getClassInfo() != null;
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
