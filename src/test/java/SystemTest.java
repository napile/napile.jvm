import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.reflect.Field;

/**
 * @author VISTALL
 * @date 18:24/19.02.2012
 */
public class SystemTest
{
	public static void main(String... arg) throws Exception
	{
		Field field = System.class.getField("out");

		Object o = field.get(null);

		Field streamField = PrintStream.class.getDeclaredField("charOut");
		streamField.setAccessible(true);

		Object objectOfOutputStream = streamField.get(o);

		Field outField = OutputStreamWriter.class.getDeclaredField("se");
		outField.setAccessible(true);

		Object objectSe = outField.get(objectOfOutputStream);

		Field f = sun.nio.cs.StreamEncoder.class.getDeclaredField("out");
		f.setAccessible(true);

		System.out.println(f.get(objectSe).getClass().getName());
	}
}
