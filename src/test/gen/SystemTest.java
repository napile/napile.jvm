/**
 * @author VISTALL
 * @date 18:24/19.02.2012
 */
public class SystemTest
{
	public static void main(String... arg) throws Exception
	{
		int b = 0;

		while (b != 3)
		{
			if (b == 0)
				Console.writeLine("b = 0");
			if (b == 1)
				Console.writeLine("b = 1");
			if (b == 2)
				Console.writeLine("b = 2") ;
			if (b == 3)
				Console.writeLine("b = 3") ;
			b = b + 1;
		}

		Console.writeLine("End") ;
	}
}
