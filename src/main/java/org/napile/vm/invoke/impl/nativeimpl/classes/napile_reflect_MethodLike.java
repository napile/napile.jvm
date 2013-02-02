package org.napile.vm.invoke.impl.nativeimpl.classes;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.asm.AsmConstants;
import org.napile.asm.Modifier;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.AnnotationNode;
import org.napile.asm.tree.members.MethodParameterNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.objects.classinfo.ReflectInfo;
import org.napile.vm.util.VmReflectUtil;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @date 18:44/31.01.13
 */
public class napile_reflect_MethodLike
{
	public static final TypeNode NAPILE_REFLECT_CALL_PARAMETER = new TypeNode(false, new ClassTypeNode(new FqName("napile.reflect.CallParameter")));

	public static final TypeNode NAPILE_LANG_ARRAY__CALL_PARAMETER__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_REFLECT_CALL_PARAMETER);

	@NativeImplement(methodName = "getCallParameters", parameters = {})
	public static BaseObjectInfo getCallParameters(Vm vm, InterpreterContext context)
	{
		BaseObjectInfo objectInfo = context.getLastStack().getObjectInfo();
		MethodInfo methodInfo = objectInfo.value();

		BaseObjectInfo array = vm.newObject(context, NAPILE_LANG_ARRAY__CALL_PARAMETER__, new TypeNode[]{AsmConstants.INT_TYPE}, new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, methodInfo.getMethodNode().parameters.size())});
		BaseObjectInfo[] value = array.value();

		int i = 0;
		for(final MethodParameterNode methodParameterNode : methodInfo.getMethodNode().parameters)
		{
			BaseObjectInfo v = VmReflectUtil.createReflectObject(NAPILE_REFLECT_CALL_PARAMETER, VmUtil.convertToVm(vm, context, null), vm, context, new ReflectInfo()
			{
				@Override
				public ClassInfo getParent()
				{
					return null;
				}

				@NotNull
				@Override
				public String getName()
				{
					return methodParameterNode.name.getName();
				}

				@NotNull
				@Override
				public FqName getFqName()
				{
					return FqName.ROOT;
				}

				@Override
				public boolean hasModifier(@NotNull Modifier modifier)
				{
					return ArrayUtil.contains(modifier, methodParameterNode.modifiers);
				}

				@Override
				public Modifier[] getModifiers()
				{
					return methodParameterNode.modifiers;
				}

				@Override
				public List<AnnotationNode> getAnnotations()
				{
					return methodParameterNode.annotations;
				}
			});

			value[i] = v;
		}

		return array;
	}
}
