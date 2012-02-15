package org.napile.vm;

import org.apache.log4j.Logger;
import org.napile.commons.logging.Log4JHelper;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.BundleUtil;
import org.napile.vm.util.cloption.CLProcessor;
import org.napile.vm.vm.VmContext;
import org.napile.vm.vm.VmInterface;
import org.napile.vm.vm.VmUtil;
import org.napile.vm.vm.impl.VmInterfaceImpl;

/**
 * @author VISTALL
 * @date 15:59/31.01.2012
 */
public class Main
{
	public static final Logger LOGGER = Logger.getLogger(Main.class);

	public static void main(String... args)
	{
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				LOGGER.info("------------ VM Stop  ------------");
			}
		}));

		Log4JHelper.load();
		final long startTime = System.currentTimeMillis();
		LOGGER.info("------------ VM Start ------------");
		BundleUtil.getInstance();

		CLProcessor p = new CLProcessor(args);

		VmContext vmContext = new VmContext();
		VmInterface vmInterface = new VmInterfaceImpl(vmContext);

		p.process(vmInterface);
		if(vmContext.getMainClass() == null)
		{
			BundleUtil.exitAbnormal(null, "main.class.not.found");
			return;
		}

		VmUtil.initBootStrap(vmInterface);
		LOGGER.info("VmUtil.initBootStrap(): " + (System.currentTimeMillis() - startTime) + " ms.");

		ClassInfo mainClass = vmInterface.getClass(vmContext.getMainClass());
		if(mainClass == null)
		{
			BundleUtil.exitAbnormal(null, "class.s1.not.found", vmContext.getMainClass());
			return;
		}

		MethodInfo methodInfo = vmInterface.getStaticMethod(mainClass, "main", "java.lang.String[]");
		if(methodInfo == null)
		{
			BundleUtil.exitAbnormal(null, "not.found.s1.s2.s3", mainClass.getName(), "main", "java.lang.String[]");
			return;
		}

		vmInterface.invokeStatic(methodInfo); //TODO [VISTALL] arguments

		System.out.println("BootLoader: " + vmInterface.getBootClassLoader().getLoadedClasses().size());
		System.out.println("CurrentLoader: " + vmInterface.getCurrentClassLoader().getLoadedClasses().size());
	}
}
