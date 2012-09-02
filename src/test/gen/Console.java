/**
 * @author VISTALL
 * @date 22:08/31.08.12
 */
public class Console
{
	public int var1;
	public int var2;
	public int var3;

	public Console()
	{
		before();

		put();
	}

	public void before()
	{
		System.out.println("before: " + var1 + "/" + var2 + "/" + var3);
	}

	public void put()
	{
		var1 = 27;
		var2 = 9;
		var3 = 1990;
	}

	public void write()
	{
		System.out.println("after: " + var1 + "/" + var2 + "/" + var3);
	}
}
