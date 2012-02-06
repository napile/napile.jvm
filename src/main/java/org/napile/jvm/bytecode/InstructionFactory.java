package org.napile.jvm.bytecode;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.jreversepro.jvm.JVMInstructionSet;
import org.napile.jvm.bytecode.impl.wide;
import org.napile.jvm.util.DumpUtil;
import org.napile.jvm.util.ExitUtil;
import com.sun.tools.javac.jvm.Mneumonics;

/**
 * @author VISTALL
 * @date 10:40/05.02.2012
 */
public class InstructionFactory
{
	public static Instruction[] parseByteCode(String name, String methodName, byte[] data)
	{
		List<Instruction> instructions = new ArrayList<Instruction>(data.length / 2);

		ByteBuffer byteBuffer = ByteBuffer.wrap(data);

		List<String> parsed = new ArrayList<String>(instructions.size());

		Instruction currentInstruction = null;
		
		while(byteBuffer.hasRemaining())
		{
			int oldPos = byteBuffer.position();
			int opcode = byteBuffer.get() & 0xFF;

			try
			{
				String text = Mneumonics.mnem[opcode];

				Class<?> instClass = Class.forName("org.napile.jvm.bytecode.impl." + text);

				Instruction instruction = (Instruction)instClass.newInstance();

				boolean iswide = currentInstruction instanceof wide;

				instruction.parseData(byteBuffer, iswide);

				int diff = byteBuffer.position() - oldPos;
				parsed.add(byteBuffer.position() + " <" + Integer.toHexString(opcode).toUpperCase() + "> " + instClass.getName() + ": " + (diff - 1));

				int size = JVMInstructionSet.getOpcodeLength(opcode, iswide);
				if(diff != size)
				{
					System.out.println("incorrect lenght for: " + instClass.getName() + " need " + (size - 1)+ " get " + (diff - 1) + " wide: " + iswide);
					System.exit(-1);
				}

				currentInstruction = instruction;
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

				for(String in : parsed)
					System.out.println(in);

				System.out.println(DumpUtil.printData(data, data.length));
				ExitUtil.exitAbnormal(e, "error.in.code.pre.code.s1.file.s2.position.s3.method.s4", code, name, byteBuffer.position(), methodName);
				break;
			}
		}

		return null;
	}
}
