/*
 * Copyright 2010-2012 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.vm;

import org.apache.log4j.Logger;
import org.napile.commons.logging.Log4JHelper;
import org.napile.vm.invoke.impl.nativeimpl.NativeWrapper;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.BundleUtil;
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
				LOGGER.debug("------------ VM Stop  ------------");
			}
		}));

		Log4JHelper.load();
		long startTime = System.currentTimeMillis();
		LOGGER.debug("------------ VM Start ------------");
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
		LOGGER.debug("VmUtil.initBootStrap(): " + (System.currentTimeMillis() - startTime) + " ms.");
		startTime = System.currentTimeMillis();
		NativeWrapper.initAll(vm);
		LOGGER.debug("NativeWrapper.initAll(): " + (System.currentTimeMillis() - startTime) + " ms.");

		ClassInfo mainClass = vm.getClass(vmContext.getMainClass());
		if(mainClass == null)
		{
			BundleUtil.exitAbnormal(null, "class.s1.not.found", vmContext.getMainClass());
			return;
		}

		MethodInfo methodInfo = vm.getStaticMethod(mainClass, "main", false/*, Vm.JAVA_LANG_STRING_ARRAY*/);
		if(methodInfo == null)
		{
			BundleUtil.exitAbnormal(null, "not.found.s1.s2.s3", mainClass.getName(), "main"/*, Vm.JAVA_LANG_STRING_ARRAY*/);
			return;
		}

		//TODO [VISTALL] invalid for now
		//ClassInfo javaClassString = vm.getClass(Vm.JAVA_LANG_STRING);
		//ClassInfo javaClassStringArray = vm.getClass(Vm.JAVA_LANG_STRING_ARRAY);

		//List<String> arguments = vmContext.getArguments();
		//ObjectInfo[] data = new ObjectInfo[arguments.size()];
		//for(int i = 0; i < data.length; i++)
		//	data[i] = VmUtil.convertToVm(vm, javaClassString, arguments.get(i));

		//System.out.println(DumpUtil.dump(data[0]));
		vm.invoke(methodInfo, null, null);
	}

	public static boolean isSupported(int major, int minor)
	{
		return major >= 43 && minor >= 0;
	}
}
