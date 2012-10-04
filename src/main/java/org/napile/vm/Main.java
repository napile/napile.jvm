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

import java.util.Collections;

import org.apache.log4j.Logger;
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

		BaseObjectInfo arrayObject = vm.newObject(interpreterContext, VmUtil.ARRAY__STRING__, VmUtil.varargTypes(VmUtil.INT), new BaseObjectInfo[]{VmUtil.convertToVm(vm, interpreterContext, vmContext.getArguments().size())});
		BaseObjectInfo[] arrayOfObjects = arrayObject.value();
		for(int i = 0; i < arrayOfObjects.length; i++)
			arrayOfObjects[i] = VmUtil.convertToVm(vm, interpreterContext, vmContext.getArguments().get(i));

		vm.invoke(new InterpreterContext(new StackEntry(null, methodInfo, new BaseObjectInfo[] {arrayObject}, Collections.<TypeNode>emptyList())));
	}
}
