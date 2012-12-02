package org.napile.vm.invoke.impl.nativeimpl.classes;

import java.util.List;

import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.VariableInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 15:22/03.11.12
 */
public class napile_reflect_Class
{
	public static final TypeNode NAPILE_REFLECT_VARIABLE = new TypeNode(false, new ClassTypeNode(new FqName("napile.reflect.Variable")));

	public static final TypeNode NAPILE_REFLECT_METHOD = new TypeNode(false, new ClassTypeNode(new FqName("napile.reflect.Method")));

	public static final TypeNode NAPILE_REFLECT_MACRO = new TypeNode(false, new ClassTypeNode(new FqName("napile.reflect.Method")));

	public static final TypeNode ARRAY__NAPILE_REFLECT_VARIABLE__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_REFLECT_VARIABLE);

	public static final TypeNode ARRAY__NAPILE_REFLECT_METHOD__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_REFLECT_METHOD);

	public static final TypeNode ARRAY__NAPILE_REFLECT_MACRO__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_REFLECT_MACRO);

	@NativeImplement(className = "napile.reflect.Class", methodName = "variables$lazy", parameters = {})
	public static BaseObjectInfo variables$lazy(Vm vm, InterpreterContext context)
	{
		StackEntry stackEntry = context.getLastStack();

		BaseObjectInfo baseObjectInfo = AssertUtil.assertNull(stackEntry.getObjectInfo());

		// type of object is - napile.reflect.Class<napile.lang.Int>
		ClassInfo classInfo = vm.safeGetClass(((ClassTypeNode)baseObjectInfo.getTypeNode().arguments.get(0).typeConstructorNode).className);

		List<VariableInfo> vars = classInfo.getVariables();

		BaseObjectInfo arrayObject = vm.newObject(context, ARRAY__NAPILE_REFLECT_VARIABLE__, VmUtil.varargTypes(VmUtil.INT), new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, vars.size())});
		BaseObjectInfo[] array = arrayObject.value();

		int i = 0;
		for(VariableInfo variableInfo : vars)
			array[i ++] = vm.newObject(context, NAPILE_REFLECT_VARIABLE, VmUtil.varargTypes(VmUtil.STRING), new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, variableInfo.getShortName())});

		return arrayObject;
	}
}
