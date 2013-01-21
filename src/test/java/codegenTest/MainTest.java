package codegenTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.commons.logging.Log4JHelper;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.classes.napile_lang_Thread;
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
 * @date 19:14/05.10.12
 */
public class MainTest
{
	public static void run(String str)
	{
		Log4JHelper.load();

		BundleUtil.getInstance();

		List<String> arguments = new ArrayList<String>();
		arguments.add("-cp");
		arguments.add("dist/classpath;dist/codegenTest.nzip");
		arguments.add("codegenTest." + str);

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

		for(String a : arguments)
			list.add(VmUtil.convertToVm(vm, interpreterContext, a));

		BaseObjectInfo arrayObject = vm.newObject(interpreterContext, VmUtil.ARRAY__STRING__, VmUtil.varargTypes(VmUtil.INT), new BaseObjectInfo[]{VmUtil.convertToVm(vm, interpreterContext, list.size())});
		BaseObjectInfo[] arrayOfObjects = arrayObject.value();
		for(int i = 0; i < list.size(); i++)
			arrayOfObjects[i] = list.get(i);

		vm.invoke(new InterpreterContext(new StackEntry(null, methodInfo, new BaseObjectInfo[] {arrayObject}, Collections.<TypeNode>emptyList())), methodInfo.getInvokeType());

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
