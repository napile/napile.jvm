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
	public void test5()
	{
		run("ifTest.HelloWorldWithIf");
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
	public void test8()
	{
		run("multi.AB");
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

	@Test
	public void test18()
	{
		run("isTest.IsTest");
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

	@Test
	public void test22()
	{
		run("tryCatchTest.TryCatchTest");
	}

	@Test
	public void test23()
	{
		run("postfixTest.PostfixTest");
	}
}
