package org.napile.vm;

import org.apache.log4j.Logger;
import org.napile.commons.logging.Log4JHelper;
import org.napile.vm.invoke.impl.nativeimpl.NativeWrapper;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.objectinfo.ObjectInfo;
import org.napile.vm.objects.objectinfo.impl.ArrayObjectInfo;
import org.napile.vm.util.BundleUtil;
import org.napile.vm.util.DumpUtil;
import org.napile.vm.util.cloption.CLProcessor;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmContext;
import org.napile.vm.vm.VmUtil;

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
		long startTime = System.currentTimeMillis();
		LOGGER.info("------------ VM Start ------------");
		BundleUtil.getInstance();

		CLProcessor p = new CLProcessor(args);

		VmContext vmContext = new VmContext();
		Vm vm = new Vm(vmContext);

		p.process(vm);
		if(vmContext.getMainClass() == null)
		{
			BundleUtil.exitAbnormal(null, "main.class.not.found");
			return;
		}

		VmUtil.initBootStrap(vm);
		LOGGER.info("VmUtil.initBootStrap(): " + (System.currentTimeMillis() - startTime) + " ms.");
		startTime = System.currentTimeMillis();
		NativeWrapper.initAll(vm);
		LOGGER.info("NativeWrapper.initAll(): " + (System.currentTimeMillis() - startTime) + " ms.");

		ClassInfo mainClass = vm.getClass(vmContext.getMainClass());
		if(mainClass == null)
		{
			BundleUtil.exitAbnormal(null, "class.s1.not.found", vmContext.getMainClass());
			return;
		}

		MethodInfo methodInfo = vm.getStaticMethod(mainClass, "main", false, Vm.JAVA_LANG_STRING_ARRAY);
		if(methodInfo == null)
		{
			BundleUtil.exitAbnormal(null, "not.found.s1.s2.s3", mainClass.getName(), "main", Vm.JAVA_LANG_STRING_ARRAY);
			return;
		}

		ClassInfo javaClassString = vm.getClass(Vm.JAVA_LANG_STRING);
		ClassInfo javaClassStringArray = vm.getClass(Vm.JAVA_LANG_STRING_ARRAY);

		ObjectInfo[] data = new ObjectInfo[args.length];
		for(int i = 0; i < args.length; i++)
			data[i] = VmUtil.convertToVm(vm, javaClassString, args[i]);

		System.out.println(DumpUtil.dump(data[0]));
		vm.invoke(methodInfo, null, null, new ArrayObjectInfo(null, javaClassStringArray, data));
	}

	public static boolean isSupported(int major, int minor)
	{
		return major >= 43 && minor >= 0;
	}
}
