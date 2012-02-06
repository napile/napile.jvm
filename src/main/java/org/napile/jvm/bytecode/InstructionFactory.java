package org.napile.jvm.bytecode;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.napile.jvm.util.ExitUtil;
import com.sun.tools.javac.jvm.Mneumonics;

/**
 * @author VISTALL
 * @date 10:40/05.02.2012
 */
public class InstructionFactory
{
	public static Instruction[] parseByteCode(String name, byte[] data)
	{
		List<Instruction> instructions = new ArrayList<Instruction>(data.length / 2);

		ByteBuffer byteBuffer = ByteBuffer.wrap(data);

		while(byteBuffer.hasRemaining())
		{
			int opcode = byteBuffer.get() & 0xFF;

			try
			{
				String text = Mneumonics.mnem[opcode];

				Class<?> instClass = Class.forName("org.napile.jvm.bytecode.impl." + text);

				Instruction instruction = (Instruction)instClass.newInstance();

				instruction.parseData(byteBuffer);

				instructions.add(instruction);
			}
			catch(Exception e)
			{
				int prevCode = byteBuffer.position() == 0 ? opcode : byteBuffer.get(byteBuffer.position() - 1) & 0xFF;
				String code = null;
				try
				{
					code = Mneumonics.mnem[prevCode];
				}
				catch(Exception e1)
				{
					code = e1.getClass().getName();
				}

				ExitUtil.exitAbnormal(e, "error.in.code.pre.code.s1.file.s2.position.s3", code, name, byteBuffer.position());
				break;
			}
		}

		return null;
	}
}
