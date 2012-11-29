package codegenTest;

import org.junit.Test;

/**
 * @author VISTALL
 * @date 19:47/03.10.12
 */
@Deprecated
public class CodegenTest extends MainTest
{
	@Test
	public void test0()
	{
		run("doWhileTest.DoWhileTest");
	}

	@Test
	public void test1()
	{
		run("equalsTest.EqualTest");
	}

	@Test
	public void test2()
	{
		run("forTest.ForTest");
	}

	@Test
	public void test3()
	{
		run("forTest.ForAndBreakTest");
	}

	@Test
	public void test4()
	{
		run("compareTest.CompareIntTest");
	}

	@Test
	public void test6()
	{
		run("labelTest.LabelTest");
	}

	@Test
	public void test7()
	{
		run("labelTest.MultiLabelTest");
	}

	@Test
	public void test9()
	{
		run("whileTest.WhileTest");
	}

	@Test
	public void test10()
	{
		run("whileTest.BreakWhileTest");
	}

	@Test
	public void test11()
	{
		run("whileTest.ContinueTest");
	}

	@Test
	public void test12()
	{
		run("ArrayTest");
	}

	@Test
	public void test13()
	{
		run("HelloWorldTest");
	}


	@Test
	public void test18()
	{
		run("isTest.IsTest");
	}



	@Test
	public void test22()
	{
		run("tryCatchTest.TryCatchTest");
	}

	@Test
	public void hashCodeTest()
	{
		run("HashCodeTest");
	}
}
