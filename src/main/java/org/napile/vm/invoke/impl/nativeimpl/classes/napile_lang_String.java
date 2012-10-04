package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 17:54/03.10.12
 */
public class napile_lang_String
{
	@NativeImplement(className = "napile.lang.String", methodName = "toString", parameters = {})
	public static BaseObjectInfo toString(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();

		VariableInfo variableInfo = vm.getField(objectInfo.getClassInfo(), "array", false);

		AssertUtil.assertNull(variableInfo);

		BaseObjectInfo varValue = objectInfo.getVarValue(variableInfo);

		BaseObjectInfo[] array = varValue.value();

		StringBuilder b = new StringBuilder();
		// iterate BaseObjectInfo of Char
		for(BaseObjectInfo o : array)
			b.append(o.value());
		return VmUtil.convertToVm(vm, context, b.toString());
	}
}
