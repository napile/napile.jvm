package codegenTest;

import org.junit.Test;

/**
 * @author VISTALL
 * @date 19:57/05.01.13
 */
public class MultiTypeTest extends MainTest
{
	@Test
	public void test()
	{
		run("multiTypeTest.Test");
	}

	@Test
	public void test1()
	{
		run("multiTypeTest.InstanceVarTest");
	}
}
