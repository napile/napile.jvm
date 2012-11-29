package codegenTest;

import org.junit.Test;

/**
 * @author VISTALL
 * @date 11:46/29.11.12
 */
public class IfTest extends MainTest
{
	@Test
	public void test5()
	{
		run("ifTest.HelloWorldWithIf");
	}

	@Test
	public void test19()
	{
		run("ifTest.OrOrTest");
	}

	@Test
	public void test20()
	{
		run("ifTest.AndAndTest");
	}

	@Test
	public void test21()
	{
		run("ifTest.ElvisTest");
	}
}
