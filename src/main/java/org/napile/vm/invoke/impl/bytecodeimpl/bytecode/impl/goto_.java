package org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl;

import gnu.trove.map.TIntIntMap;

import java.nio.ByteBuffer;

import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.Instruction;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 4:52/06.02.2012
 */
public class goto_ extends Instruction
{
	private int _index;
	private int _positionToGo;

	@Override
	public void parseData(ByteBuffer buffer, boolean wide)
	{
		_index = buffer.getShort() + getArrayIndex();
	}

	@Override
	public void call(Vm vm, InterpreterContext context)
	{
		//
	}

	@Override
	public int getNextIndex(int index)
	{
		return _positionToGo;
	}

	@Override
	public void findIndexes(TIntIntMap map)
	{
		_positionToGo = map.get(_index);
	}

	@Override
	public String toString()
	{
		return super.toString() + " [a" + _index + ", i" + _positionToGo + "]";
	}
}
