package codegenTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 * @date 19:14/05.10.12
 */
public class MainTest
{
	public static void run(String str)
	{
		Log4JHelper.load();

		BundleUtil.getInstance();

		List<String> arg = new ArrayList<String>();
		arg.add("-cp");
		arg.add("dist/classpath;dist/codegenTest.nzip");
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
