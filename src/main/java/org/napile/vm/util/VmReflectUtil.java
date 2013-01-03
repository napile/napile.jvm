package org.napile.vm.util;

import org.napile.asm.AsmConstants;
import org.napile.asm.Modifier;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.lib.NapileReflectPackage;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ReflectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 15:33/31.12.12
 */
public class VmReflectUtil
{
	public static final TypeNode NAPILE_LANG_ARRAY__MODIFIER__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(new TypeNode(false, new ClassTypeNode(NapileReflectPackage.MODIFIER)));
	public static final TypeNode NAPILE_LANG_ARRAY__ANY__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(AsmConstants.ANY_TYPE);
	public static final TypeNode NAPILE_REFLECT_CLASS__ANY__NULLABLE_ = new TypeNode(true, new ClassTypeNode(NapileReflectPackage.CLASS)).visitArgument(AsmConstants.ANY_TYPE);

	public static final TypeNode CONSTRUCTOR = new TypeNode(false, new ClassTypeNode(NapileReflectPackage.CONSTRUCTOR));
	public static final TypeNode METHOD = new TypeNode(false, new ClassTypeNode(NapileReflectPackage.METHOD));
	public static final TypeNode MACRO = new TypeNode(false, new ClassTypeNode(NapileReflectPackage.MACRO));

	public static BaseObjectInfo createReflectObject(TypeNode typeNode, BaseObjectInfo owner, Vm vm, InterpreterContext context, ReflectInfo reflectInfo)
	{
		return vm.newObject
		(
				context,
				typeNode,
				VmUtil.varargTypes
						(
								NAPILE_REFLECT_CLASS__ANY__NULLABLE_,
								VmUtil.STRING,
								NAPILE_LANG_ARRAY__MODIFIER__,
								NAPILE_LANG_ARRAY__ANY__
						),
				new BaseObjectInfo[]
						{
								owner,
								VmUtil.convertToVm(vm, context, reflectInfo.getName()),
								createArray$Modifier$(vm, context, reflectInfo),
								createArray$Any$Annotations(vm, context, reflectInfo)
						}
		);
	}

	public static BaseObjectInfo createArray$Modifier$(Vm vm, InterpreterContext context, ReflectInfo memberNode)
	{
		BaseObjectInfo baseObjectInfo = vm.newObject(context, NAPILE_LANG_ARRAY__MODIFIER__, new TypeNode[]{AsmConstants.INT_TYPE}, new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, memberNode.getModifiers().length)});
		BaseObjectInfo[] value = baseObjectInfo.value();

		int i = 0;
		for(Modifier modifier : memberNode.getModifiers())
			value[i] = VmUtil.staticValue(vm, NapileReflectPackage.MODIFIER, modifier.name());

		return baseObjectInfo;
	}

	public static BaseObjectInfo createArray$Any$Annotations(Vm vm, InterpreterContext context, ReflectInfo memberNode)
	{
		BaseObjectInfo baseObjectInfo = vm.newObject(context, NAPILE_LANG_ARRAY__ANY__, new TypeNode[]{AsmConstants.INT_TYPE}, new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, 0)});
		BaseObjectInfo[] value = baseObjectInfo.value();

		//TODO [VISTALL] annotations invoking

		return baseObjectInfo;
	}
}
