/**
 * @author VISTALL
 * @date 18:24/19.02.2012
 */
public class SystemTest
{
	SystemTest()
	{
		if(Boolean.TRUE)
			System.out.println("true");
		else
			System.out.println("false");
	}

	public static void main(String... arg) throws Exception
	{
		Console console = new Console();

		console.write();
	}
}
