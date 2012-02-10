package org.napile.jvm;

import org.apache.log4j.Logger;
import org.napile.commons.logging.Log4JHelper;
import org.napile.jvm.localize.LocalizeMaker;
import org.napile.jvm.objects.classinfo.ClassInfo;
import org.napile.jvm.objects.classinfo.MethodInfo;
import org.napile.jvm.util.ExitUtil;
import org.napile.jvm.util.cloption.CLProcessor;
import org.napile.jvm.vm.VmContext;
import org.napile.jvm.vm.VmInterface;
import org.napile.jvm.vm.VmUtil;
import org.napile.jvm.vm.impl.VmInterfaceImpl;

/**
 * @author VISTALL
 * @date 15:59/31.01.2012
 */
public class Main
{
	private static final Logger LOGGER = Logger.getLogger(ExitUtil.class);

	public static void main(String... args)
	{
		Log4JHelper.load();
		LocalizeMaker.getInstance();

		CLProcessor p = new CLProcessor(args);

		VmContext vmContext = new VmContext();
		VmInterface vmInterface = new VmInterfaceImpl(vmContext);

		p.process(vmInterface);
		if(vmContext.getMainClass() == null)
		{
			ExitUtil.exitAbnormal(null, "Not find main class.");
			return;
		}

		VmUtil.initBootStrap(vmInterface);

		ClassInfo mainClass = vmInterface.getClass(vmContext.getMainClass());
		if(mainClass == null)
		{
			ExitUtil.exitAbnormal(null, "class.s1.not.found", vmContext.getMainClass());
			return;
		}

		MethodInfo methodInfo = vmInterface.getStaticMethod(mainClass, "main", "java.lang.String[]");
		if(methodInfo == null)
		{
			ExitUtil.exitAbnormal(null, "not.found.s1.s2.s3", mainClass.getName(), "main", "java.lang.String[]");
			return;
		}

		System.out.println(vmInterface.getBootClassLoader().getLoadedClasses().size());

		//Instruction[] instructions = InstructionFactory.parseByteCode("test", methodInfo.toString(), ((MethodInfoImpl)methodInfo).getBytecode());
		//if(LOGGER.isDebugEnabled())
		//	vmContext.print();
	}
}
