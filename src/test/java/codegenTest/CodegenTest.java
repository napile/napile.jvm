package codegenTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.commons.logging.Log4JHelper;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.BundleUtil;
import org.napile.vm.util.cloption.CLProcessor;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmContext;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 19:47/03.10.12
 */
public class CodegenTest
{
	@Test
	public void test0()
	{
		runMain("doWhileTest.DoWhileTest");
	}

	@Test
	public void test1()
	{
		runMain("equalsTest.EqualTest");
	}

	@Test
	public void test2()
	{
		runMain("forTest.ForTest");
	}

	@Test
	public void test3()
	{
		runMain("forTest.ForAndBreakTest");
	}

	@Test
	public void test4()
	{
		runMain("forTest.ForAndBreakTest");
	}

	@Test
	public void test5()
	{
		runMain("ifTest.HelloWorldWithIf");
	}

	@Test
	public void test6()
	{
		runMain("labelTest.LabelTest");
	}

	@Test
	public void test7()
	{
		runMain("labelTest.MultiLabelTest");
	}

	@Test
	public void test8()
	{
		runMain("multi.AB");
	}

	@Test
	public void test9()
	{
		runMain("whileTest.WhileTest");
	}

	@Test
	public void test10()
	{
		runMain("whileTest.BreakWhileTest");
	}

	@Test
	public void test11()
	{
		runMain("whileTest.ContinueTest");
	}

	@Test
	public void test12()
	{
		runMain("ArrayTest");
	}

	@Test
	public void test13()
	{
		runMain("HelloWorldTest");
	}

	@Test
	public void test14()
	{
		runMain("classAndTypeTest.ClassOfTest");
	}

	@Test
	public void test15()
	{
		runMain("classAndTypeTest.GenericTest");
	}

	@Test
	public void test16()
	{
		//runMain("classAndTypeTest.TypeOfTest");
	}

	@Test
	public void test17()
	{
		runMain("classAndTypeTest.GenericInMethod");
	}

	@Test
	public void test18()
	{
		runMain("isTest.IsTest");
	}

	@Test
	public void test19()
	{
		runMain("ifTest.OrOrTest");
	}

	@BeforeClass
	public static void before()
	{
		Log4JHelper.load();

		BundleUtil.getInstance();
	}

	public static void runMain(String str)
	{
		List<String> arg = new ArrayList<String>();
		arg.add("-cp");
		arg.add("dist/classpath");
		arg.add("codegenTest." + str);

		CLProcessor p = new CLProcessor(arg.toArray(new String[arg.size()]));

		VmContext vmContext = new VmContext();
		Vm vm = new Vm(vmContext);

		p.process(vm);

		if(vmContext.getMainClass() == null)
		{
			BundleUtil.exitAbnormal(null, "main.class.not.found");
			return;
		}

		VmUtil.initBootStrap(vm);

		ClassInfo mainClass = vm.getClass(vmContext.getMainClass());
		if(mainClass == null)
		{
			BundleUtil.exitAbnormal(null, "class.s1.not.found", vmContext.getMainClass());
			return;
		}

		MethodInfo methodInfo = vm.getStaticMethod(mainClass, "main", false, VmUtil.ARRAY__STRING__);
		if(methodInfo == null)
		{
			BundleUtil.exitAbnormal(null, "not.found.s1.s2.s3", mainClass.getName(), "main", VmUtil.ARRAY__STRING__);
			return;
		}

		InterpreterContext interpreterContext = new InterpreterContext();

		BaseObjectInfo arrayObject = vm.newObject(interpreterContext, VmUtil.ARRAY__STRING__, VmUtil.varargTypes(VmUtil.INT), new BaseObjectInfo[] {VmUtil.convertToVm(vm, interpreterContext, vmContext.getArguments().size())});
		BaseObjectInfo[] arrayOfObjects = arrayObject.value();
		for(int i = 0; i < arrayOfObjects.length; i++)
			arrayOfObjects[i] = VmUtil.convertToVm(vm, interpreterContext, vmContext.getArguments().get(i));

		vm.invoke(new InterpreterContext(new StackEntry(null, methodInfo, new BaseObjectInfo[] {arrayObject}, Collections.<TypeNode>emptyList())));
	}
}