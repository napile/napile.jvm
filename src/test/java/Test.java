/**
 * @author VISTALL
 * @since 11:56/04.01.13
 */
public class Test
{
	public static void main(String...arg) throws Exception
	{

		int[] a = new int[1];

		int index = 0;
		int length = a.length;
		if(index < 0 || index >= length)
		{
			throw new UnsupportedOperationException();
		}

		int d = a[index];
	}
}
