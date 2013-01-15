package org.napile.vm.invoke.impl.nativeimpl.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.napile.asm.tree.members.types.TypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 19:29/14.01.13
 */
public class napile_lang_Thread
{
	private static final ThreadGroup THREAD_GROUP = new ThreadGroup("napile-threads");
	public static List<Thread> THREADS = new ArrayList<Thread>();

	@NativeImplement(methodName = "start0", parameters = {})
	public static void start0(final Vm vm, final InterpreterContext context)
	{
		final BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		Thread thread = new Thread(THREAD_GROUP, new Runnable()
		{
			@Override
			public void run()
			{
				MethodInfo methodInfo = vm.getMethod(objectInfo.getClassInfo(), "run", false, new TypeNode[0]);
				StackEntry stackEntry = new StackEntry(objectInfo, methodInfo, BaseObjectInfo.EMPTY_ARRAY, Collections.<TypeNode>emptyList());
				vm.invoke(new InterpreterContext(stackEntry));
			}
		});

		THREADS.add(thread);

		thread.start();
	}
}
