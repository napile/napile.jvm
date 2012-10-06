package codegenTest;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author VISTALL
 * @date 17:27/06.10.12
 */
public class ArrayUtilTest extends MainTest
{
	@Test
	public void test1()
	{
		run("arrayUtilTest.ArrayUtilTest");

		System.out.println();
		System.out.println("----------------------");
		System.out.println("java");
		System.out.println("----------------------");

		String[] array = new String[10];
		int i = 0;
		for(String a : array)
			array[i++] = String.valueOf(i);

		for(String a : array)
		{
			System.out.print(a);
			System.out.print("-");
		}

		System.out.println();
		System.out.println("After copyOfRange");

		String[] newArray = Arrays.copyOfRange(array, 0, 5);
		for(String a : newArray)
		{
			System.out.print(a);
			System.out.print("-");
		}
	}

	@Test
	public void test2()
	{
		run("arrayUtilTest.ArrayUtilCopyOfTest");

		System.out.println();
		System.out.println("----------------------");
		System.out.println("java");
		System.out.println("----------------------");

		String[] array = new String[10];
		int i = 0;
		for(String a : array)
			array[i++] = String.valueOf(i);

		for(String a : array)
		{
			System.out.print(a);
			System.out.print("-");
		}

		System.out.println();
		System.out.println("After copyOf");

		String[] newArray = Arrays.copyOf(array, 7);
		for(String a : newArray)
		{
			System.out.print(a);
			System.out.print("-");
		}
	}
}
