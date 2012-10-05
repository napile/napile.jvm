package codegenTest;

/**
 * @author VISTALL
 * @date 13:47/05.10.12
 */
public class AA
{
	String test()
	{
		String a = "in";
		try
		{
			a = "try";
			return a;
		}
		finally
		{
			a = "finally";
			return a;
		}
	}
}
