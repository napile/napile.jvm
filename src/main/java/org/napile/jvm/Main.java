package org.napile.jvm;

import org.apache.log4j.Logger;
import org.napile.commons.logging.Log4JHelper;
import org.napile.jvm.localize.LocalizeMaker;
import org.napile.jvm.objects.classinfo.ClassInfo;
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

		VmContext vmContext = p.process();
		if(vmContext.getMainClass() == null)
		{
			ExitUtil.exitAbnormal("Not find main class.");
			return;
		}

		VmInterface vmInterface = new VmInterfaceImpl(vmContext);
		VmUtil.initBootStrap(vmInterface);

		ClassInfo mainClass = vmInterface.getClass(vmContext.getMainClass());
		if(mainClass == null)
		{
			ExitUtil.exitAbnormal("class.s1.not.found", vmContext.getMainClass());
			return;
		}

		vmContext.print();
	}
}
