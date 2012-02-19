package org.napile.vm.invoke.impl.bytecodeimpl.bytecode;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl.wide;
import com.sun.tools.javac.jvm.Mneumonics;

/**
 * @author VISTALL
 * @date 10:40/05.02.2012
 */
public class InstructionFactory
{
	public static Instruction[] parseByteCode(byte[] data)
	{
		List<Instruction> instructions = new ArrayList<Instruction>(data.length / 2);

		ByteBuffer byteBuffer = ByteBuffer.wrap(data);

		TIntIntMap map = new TIntIntHashMap();

		Instruction currentInstruction = null;

		while(byteBuffer.hasRemaining())
		{
			int indexInArray = byteBuffer.position();
			int opcode = byteBuffer.get() & 0xFF;

			try
			{
				String text = Mneumonics.mnem[opcode];

				Class<?> instClass = Class.forName("org.napile.vm.invoke.impl.bytecodeimpl.bytecode.impl." + text);

				Instruction instruction = (Instruction)instClass.newInstance();

				boolean iswide = currentInstruction instanceof wide;

				instruction.setArrayIndex(indexInArray);
				instruction.setInstructionIndex(instructions.size());

				instruction.parseData(byteBuffer, iswide);

				currentInstruction = instruction;

				map.put(instruction.getArrayIndex(), instruction.getInstructionIndex());
				instructions.add(instruction);
			}
			catch(Exception e)
			{
				//
			}
		}

		for(Instruction instruction : instructions)
			instruction.findIndexes(map);

		return instructions.isEmpty() ? Instruction.EMPTY_ARRAY : instructions.toArray(new Instruction[instructions.size()]);
	}
}
