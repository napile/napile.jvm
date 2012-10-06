package codegenTest;

import org.junit.Test;

/**
 * @author VISTALL
 * @date 16:14/06.10.12
 */
public class PostfixTest extends MainTest
{
	@Test
	public void firstTest()
	{
		run("postfixTest.PostfixTest");
	}

	@Test
	public void withProperty()
	{
		run("postfixTest.PostfixWithPropertyTest");
	}

	@Test
	public void arrayAndPropertyTest()
	{
		run("postfixTest.PostfixInArrayAndPropertyTest");
	}
}
