package org.napile.vm.invoke.impl.bytecodeimpl.bytecode;

import gnu.trove.map.TIntIntMap;

import org.dom4j.Element;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 10:16/05.02.2012
 */
public abstract class Instruction
{
	public static final Instruction[] EMPTY_ARRAY = new Instruction[0];

	private int _instructionIndex;
	private int _arrayIndex;

	public abstract void parseData(Element element);

	public abstract void call(Vm vm, InterpreterContext context);

	public void findIndexes(TIntIntMap map)
	{
		//
	}

	public int getNextIndex(int index)
	{
		return index + 1;
	}

	public void setArrayIndex(int arrayIndex)
	{
		_arrayIndex = arrayIndex;
	}

	public int getArrayIndex()
	{
		return _arrayIndex;
	}

	public int getInstructionIndex()
	{
		return _instructionIndex;
	}

	public void setInstructionIndex(int instructionIndex)
	{
		_instructionIndex = instructionIndex;
	}

	@Override
	public String toString()
	{
		return "[a" + _arrayIndex + ", " + "i" + _instructionIndex + "]: " + getClass().getSimpleName();
	}
}
