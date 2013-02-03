package org.napile.vm.invoke.impl.nativeimpl.classes;

import org.napile.asm.AsmConstants;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.asm.tree.members.types.constructors.TypeParameterValueTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.CallParameterInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 18:22/02.02.13
 */
public class napile_reflect_CallParameter
{
	public static final TypeNode NAPILE_ASM_MEMBERS_ASM_TYPE = new TypeNode(false, new ClassTypeNode(new FqName("napile.asm.members.AsmType")));

	public static final TypeNode NAPILE_ASM_MEMBERS_ASM_ANNOTATION = new TypeNode(false, new ClassTypeNode(new FqName("napile.asm.members.AsmAnnotation")));

	public static final TypeNode NAPILE_ASM_MEMBERS_ASM_TYPE_CONSTRUCTOR = new TypeNode(false, new ClassTypeNode(new FqName("napile.asm.members.AsmTypeConstructor")));

	public static final TypeNode NAPILE_ASM_MEMBERS_ASM_TYPE_CONSTRUCTOR_ASM_CLASS_TYPE = new TypeNode(false, new ClassTypeNode(new FqName("napile.asm.members.typeConstructor.AsmClassType")));

	public static final TypeNode NAPILE_ASM_MEMBERS_ASM_TYPE_CONSTRUCTOR_ASM_TYPE_PARAMETER_TYPE = new TypeNode(false, new ClassTypeNode(new FqName("napile.asm.members.typeConstructor.AsmTypeParameterType")));

	public static final TypeNode NAPILE_LANG_ARRAY__ASM_TYPE__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_ASM_MEMBERS_ASM_TYPE);

	public static final TypeNode NAPILE_LANG_ARRAY__ASM_ANNOTATION__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_ASM_MEMBERS_ASM_ANNOTATION);

	private static final TypeNode[] ASM_TYPE_PARAMETERS = new TypeNode[] {NAPILE_LANG_ARRAY__ASM_ANNOTATION__, NAPILE_ASM_MEMBERS_ASM_TYPE_CONSTRUCTOR, NAPILE_LANG_ARRAY__ASM_TYPE__, AsmConstants.BOOL_TYPE};

	@NativeImplement(methodName = "getReturnType", parameters = {})
	public static BaseObjectInfo getReturnType(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		CallParameterInfo methodInfo = objectInfo.value();
		TypeNode typeNode = methodInfo.getReturnType();

		return convertType(vm, context, typeNode);
	}

	private static BaseObjectInfo convertType(Vm vm, InterpreterContext context, TypeNode typeNode)
	{
		//TODO [VISTALL] annotations
		BaseObjectInfo annotationArray = VmUtil.createArray(vm, NAPILE_LANG_ARRAY__ASM_ANNOTATION__, 0);
		BaseObjectInfo constructorObject = null;
		if(typeNode.typeConstructorNode instanceof ClassTypeNode)
			constructorObject = vm.newObject(context, NAPILE_ASM_MEMBERS_ASM_TYPE_CONSTRUCTOR_ASM_CLASS_TYPE, new TypeNode[]{AsmConstants.STRING_TYPE}, new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, ((ClassTypeNode) typeNode.typeConstructorNode).className.getFqName())});
		else if(typeNode.typeConstructorNode instanceof TypeParameterValueTypeNode)
			constructorObject = vm.newObject(context, NAPILE_ASM_MEMBERS_ASM_TYPE_CONSTRUCTOR_ASM_TYPE_PARAMETER_TYPE, new TypeNode[]{AsmConstants.STRING_TYPE}, new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, ((TypeParameterValueTypeNode) typeNode.typeConstructorNode).name)});
		else
			throw new UnsupportedOperationException("Unknown how convert constructor " + typeNode.typeConstructorNode);

		BaseObjectInfo parametersArray = VmUtil.createArray(vm, NAPILE_LANG_ARRAY__ASM_TYPE__, typeNode.arguments.size());
		BaseObjectInfo[] parametersArrayValue = parametersArray.value();
		for(int i = 0; i < parametersArrayValue.length; i++)
			parametersArrayValue[i] = convertType(vm, context, typeNode.arguments.get(i));

		return vm.newObject(context, NAPILE_ASM_MEMBERS_ASM_TYPE, ASM_TYPE_PARAMETERS, new BaseObjectInfo[]{annotationArray, constructorObject, parametersArray, VmUtil.convertToVm(vm, context, typeNode.nullable)});
	}
}
