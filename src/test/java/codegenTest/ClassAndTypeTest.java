package codegenTest;

import org.junit.Test;

/**
 * @author VISTALL
 * @date 16:19/23.11.12
 */
public class ClassAndTypeTest extends MainTest
{
	@Test
	public void test14()
	{
		run("classAndTypeTest.ClassOfTest");
	}

	@Test
	public void test15()
	{
		run("classAndTypeTest.GenericTest");
	}

	@Test
	public void test16()
	{
		//runMain("classAndTypeTest.TypeOfTest");
	}

	@Test
	public void test17()
	{
		run("classAndTypeTest.GenericInMethod");
	}
}
