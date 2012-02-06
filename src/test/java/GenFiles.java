import java.io.File;
import java.io.FileWriter;

import com.sun.tools.javac.jvm.Mneumonics;

/**
 * @author VISTALL
 * @date 2:58/02.02.2012
 */
public class GenFiles
{
	public static void main(String... arg) throws Exception
	{
		String text = 
				"package org.napile.jvm.bytecode.impl;\n" +
				"\n" +
				"import java.nio.ByteBuffer;\n" +
				"\n" +
				"import org.napile.jvm.bytecode.Instruction;\n" +
				"import org.napile.jvm.vm.VmInterface;\n" +
				"\n" +
				"/**\n" +
				" * @author VISTALL\n" +
				" * @date 4:52/06.02.2012\n" +
				" */\n" +
				"public class %name% implements Instruction\n" +
				"{\n" +
				"\t@Override\n" +
				"\tpublic void parseData(ByteBuffer buffer)\n" +
				"\t{\n" +
				"\n" +
				"\t}\n" +
				"\n" +
				"\t@Override\n" +
				"\tpublic void call(VmInterface vmInterface)\n" +
				"\t{\n" +
				"\n" +
				"\t}\n" +
				"}\n" +
				"";

		for(String code : Mneumonics.mnem)
		{
			File f = new File("G:\\napile-jvm\\src\\main\\java\\org\\napile\\jvm\\bytecode\\impl\\" + code + ".java");
			if(f.exists())
				continue;

			FileWriter writer = new FileWriter(f);
			writer.write(text.replace("%name%", code));
			writer.close();
		}
	}
}
