package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.util.DumpUtil;
import org.napile.vm.vm.Vm;

/**
 * @author VISTALL
 * @date 22:43/07.09.12
 */
public class codegenTest_MyTest
{
	@NativeImplement(className = "codegenTest.MyTest", methodName = "prev0", parameters = {})
	public static void prev0(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		System.out.println("prev0 " + DumpUtil.dump(vm, objectInfo));
	}

	@NativeImplement(className = "codegenTest.MyTest", methodName = "prev", parameters = {})
	public static void prev(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		System.out.println("prev " + DumpUtil.dump(vm, objectInfo));
	}

	@NativeImplement(className = "codegenTest.MyTest", methodName = "after", parameters = {})
	public static void after(Vm vm, BaseObjectInfo objectInfo, BaseObjectInfo[] arg)
	{
		System.out.println("after " + DumpUtil.dump(vm, objectInfo));
	}
}
