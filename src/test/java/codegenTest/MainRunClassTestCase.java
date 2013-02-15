package codegenTest;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.SkipTest;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.classes.napile_lang_Thread;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.BundleUtil;
import org.napile.vm.util.Log4JHelper;
import org.napile.vm.util.cloption.CLProcessor;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmContext;
import org.napile.vm.vm.VmUtil;
import com.intellij.openapi.util.text.StringUtil;
import junit.framework.TestCase;

/**
 * @author VISTALL
 * @date 19:14/05.10.12
 */
public abstract class MainRunClassTestCase extends TestCase
{
	@Override
	protected void runTest() throws Throwable
	{
		assertNotNull("TestCase.fName cannot be null", getName()); // Some VMs crash when calling getMethod(null,null);
		Method runMethod = null;
		try
		{
			// use getMethod to get all public inherited
			// methods. getDeclaredMethods returns all
			// methods of this class but excludes the
			// inherited ones.
			runMethod = getClass().getMethod(getName(), (Class[]) null);
		}
		catch(NoSuchMethodException e)
		{
			fail("Method \"" + getName() + "\" not found");
		}
		if(!Modifier.isPublic(runMethod.getModifiers()))
		{
			fail("Method \"" + getName() + "\" should be public");
		}

		if(runMethod.getAnnotation(SkipTest.class) != null)
		{
			System.out.println(runMethod.getName() + " is skipped");
			return;
		}
		doTest();
	}

	protected void doTest()
	{
		Log4JHelper.load();

		BundleUtil.getInstance();

		String testName = getName();
		testName = testName.substring(4, testName.length());

		List<String> arguments = new ArrayList<String>();
		arguments.add("-cp");
		arguments.add("dist/classpath;dist/codegenTest.nzip");
		arguments.add("codegenTest." + StringUtil.decapitalize(getClass().getSimpleName()) + "." + testName);

		CLProcessor p = new CLProcessor(arguments.toArray(new String[arguments.size()]));

		VmContext vmContext = new VmContext();
		Vm vm = new Vm(vmContext);

		p.process(vm);

		if(vmContext.getMainClass() == null)
		{
			BundleUtil.exitAbnormal(null, "main.class.not.found");
			return;
		}

		VmUtil.initBootStrap(vm);

		ClassInfo vmCaller = vm.safeGetClass(VmUtil.VM_MAIN_CALLER);
		MethodInfo methodInfo = vm.getStaticMethod(vmCaller, "main", false, VmUtil.ARRAY__STRING__);

		InterpreterContext interpreterContext = new InterpreterContext();

		List<BaseObjectInfo> list = new ArrayList<BaseObjectInfo>();
		if(vmContext.getMainClass() != null)
			list.add(VmUtil.convertToVm(vm, interpreterContext, vmContext.getMainClass().getFqName()));

		for(String a : vmContext.getArguments())
			list.add(VmUtil.convertToVm(vm, interpreterContext, a));

		BaseObjectInfo arrayObject = VmUtil.createArray(vm, VmUtil.ARRAY__STRING__, list.size());
		BaseObjectInfo[] arrayOfObjects = arrayObject.value();
		for(int i = 0; i < list.size(); i++)
			arrayOfObjects[i] = list.get(i);

		vm.invoke(new InterpreterContext(new StackEntry(null, methodInfo, new BaseObjectInfo[]{arrayObject}, Collections.<TypeNode>emptyList())), methodInfo.getInvokeType());

		while(true)
		{
			boolean isAlive = false;
			for(Thread thread : napile_lang_Thread.THREADS)
				if(thread.isAlive())
				{
					isAlive = true;
					break;
				}

			if(!isAlive)
				break;

			try
			{
				Thread.sleep(1000L);
			}
			catch(InterruptedException e)
			{
				break;
			}
		}
	}
}
