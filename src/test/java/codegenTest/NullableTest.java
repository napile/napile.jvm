package codegenTest;

import org.junit.Test;

/**
 * @author VISTALL
 * @date 9:12/29.11.12
 */
public class NullableTest extends MainTest
{
	@Test
	public void test()
	{
		run("nullableTest.NullableTest");
	}

	@Test
	public void test1()
	{
		run("nullableTest.SureTest");
	}

	@Test
	public void test2()
	{
		run("nullableTest.Sure2Test");
	}
}
