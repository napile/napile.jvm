package org.napile.vm.invoke.impl.nativeimpl.classes;

import java.util.ArrayList;
import java.util.List;

import org.napile.asm.AsmConstants;
import org.napile.asm.lib.NapileLangPackage;
import org.napile.asm.lib.NapileReflectPackage;
import org.napile.asm.resolve.name.FqName;
import org.napile.asm.tree.members.MethodNode;
import org.napile.asm.tree.members.types.TypeNode;
import org.napile.asm.tree.members.types.constructors.ClassTypeNode;
import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.invoke.impl.bytecodeimpl.StackEntry;
import org.napile.vm.invoke.impl.nativeimpl.NativeImplement;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.objects.classinfo.ClassInfo;
import org.napile.vm.objects.classinfo.MethodInfo;
import org.napile.vm.util.AssertUtil;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @date 15:22/03.11.12
 */
public class napile_reflect_Class
{
	//public static final TypeNode NAPILE_REFLECT_VARIABLE = new TypeNode(false, new ClassTypeNode(new FqName("napile.reflect.Variable")));

	public static final TypeNode NAPILE_REFLECT_CLASS__ANY_NULLABLE__ = new TypeNode(true, new ClassTypeNode(NapileReflectPackage.CLASS)).visitArgument(AsmConstants.ANY_TYPE);

	public static final TypeNode NAPILE_REFLECT_METHOD = new TypeNode(false, new ClassTypeNode(new FqName("napile.reflect.Method")));

	public static final TypeNode NAPILE_REFLECT_MACRO = new TypeNode(false, new ClassTypeNode(new FqName("napile.reflect.Macro")));

	public static final TypeNode NAPILE_REFLECT_CONSTRUCTOR = new TypeNode(false, new ClassTypeNode(new FqName("napile.reflect.Constructor")));

	//public static final TypeNode ARRAY__NAPILE_REFLECT_VARIABLE__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_REFLECT_VARIABLE);

	public static final TypeNode ARRAY__NAPILE_REFLECT_CONSTRUCTOR__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_REFLECT_CONSTRUCTOR);

	public static final TypeNode ARRAY__NAPILE_REFLECT_METHOD__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_REFLECT_METHOD);

	public static final TypeNode ARRAY__NAPILE_REFLECT_MACRO__ = new TypeNode(false, new ClassTypeNode(NapileLangPackage.ARRAY)).visitArgument(NAPILE_REFLECT_MACRO);

	@NativeImplement(className = "napile.reflect.Class", methodName = "getMethods", parameters = {})
	public static BaseObjectInfo getMethods(Vm vm, InterpreterContext context)
	{
		StackEntry stackEntry = context.getLastStack();

		BaseObjectInfo baseObjectInfo = AssertUtil.assertNull(stackEntry.getObjectInfo());

		// type of object is - napile.reflect.Class<napile.lang.Int>
		ClassInfo classInfo = vm.safeGetClass(((ClassTypeNode)baseObjectInfo.getTypeNode().arguments.get(0).typeConstructorNode).className);

		List<MethodInfo> methods = classInfo.getMethods();

		List<BaseObjectInfo> methodObjects = new ArrayList<BaseObjectInfo>(methods.size());
		for(MethodInfo methodInfo : methods)
		{
			String name = methodInfo.getName();
			if(name.equals(MethodNode.CONSTRUCTOR_NAME.getName()) || name.equals(MethodNode.STATIC_CONSTRUCTOR_NAME.getName()))
				continue;
			methodObjects.add(vm.newObject(context, NAPILE_REFLECT_METHOD, VmUtil.varargTypes(NAPILE_REFLECT_CLASS__ANY_NULLABLE__, VmUtil.STRING), new BaseObjectInfo[]{baseObjectInfo, VmUtil.convertToVm(vm, context, methodInfo.getName())}));
		}

		BaseObjectInfo arrayObject = vm.newObject(context, ARRAY__NAPILE_REFLECT_METHOD__, VmUtil.varargTypes(VmUtil.INT), new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, methodObjects.size())});
		BaseObjectInfo[] array = arrayObject.value();
		for(int i = 0; i < methodObjects.size(); i++)
			array[i] = methodObjects.get(i);

		return arrayObject;
	}

	@NativeImplement(className = "napile.reflect.Class", methodName = "getConstructors", parameters = {})
	public static BaseObjectInfo getConstructors(Vm vm, InterpreterContext context)
	{
		StackEntry stackEntry = context.getLastStack();

		BaseObjectInfo baseObjectInfo = AssertUtil.assertNull(stackEntry.getObjectInfo());

		// type of object is - napile.reflect.Class<napile.lang.Int>
		ClassInfo classInfo = vm.safeGetClass(((ClassTypeNode)baseObjectInfo.getTypeNode().arguments.get(0).typeConstructorNode).className);

		List<MethodInfo> methods = classInfo.getMethods();

		List<BaseObjectInfo> methodObjects = new ArrayList<BaseObjectInfo>(methods.size());
		for(MethodInfo methodInfo : methods)
		{
			String name = methodInfo.getName();
			if(name.equals(MethodNode.CONSTRUCTOR_NAME.getName()))
				methodObjects.add(vm.newObject(context, NAPILE_REFLECT_CONSTRUCTOR, VmUtil.varargTypes(NAPILE_REFLECT_CLASS__ANY_NULLABLE__, VmUtil.STRING), new BaseObjectInfo[]{baseObjectInfo, VmUtil.convertToVm(vm, context, methodInfo.getName())}));
		}

		BaseObjectInfo arrayObject = vm.newObject(context, ARRAY__NAPILE_REFLECT_CONSTRUCTOR__, VmUtil.varargTypes(VmUtil.INT), new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, methodObjects.size())});
		BaseObjectInfo[] array = arrayObject.value();
		for(int i = 0; i < methodObjects.size(); i++)
			array[i] = methodObjects.get(i);

		return arrayObject;
	}

	@NativeImplement(className = "napile.reflect.Class", methodName = "getMacros", parameters = {})
	public static BaseObjectInfo getMacros(Vm vm, InterpreterContext context)
	{
		StackEntry stackEntry = context.getLastStack();

		BaseObjectInfo baseObjectInfo = AssertUtil.assertNull(stackEntry.getObjectInfo());

		// type of object is - napile.reflect.Class<napile.lang.Int>
		ClassInfo classInfo = vm.safeGetClass(((ClassTypeNode)baseObjectInfo.getTypeNode().arguments.get(0).typeConstructorNode).className);

		List<MethodInfo> methods = classInfo.getMacros();

		List<BaseObjectInfo> methodObjects = new ArrayList<BaseObjectInfo>(methods.size());
		for(MethodInfo methodInfo : methods)
			methodObjects.add(vm.newObject(context, NAPILE_REFLECT_MACRO, VmUtil.varargTypes(NAPILE_REFLECT_CLASS__ANY_NULLABLE__, VmUtil.STRING), new BaseObjectInfo[]{baseObjectInfo, VmUtil.convertToVm(vm, context, methodInfo.getName())}));

		BaseObjectInfo arrayObject = vm.newObject(context, ARRAY__NAPILE_REFLECT_MACRO__, VmUtil.varargTypes(VmUtil.INT), new BaseObjectInfo[]{VmUtil.convertToVm(vm, context, methodObjects.size())});
		BaseObjectInfo[] array = arrayObject.value();
		for(int i = 0; i < methodObjects.size(); i++)
			array[i] = methodObjects.get(i);

		return arrayObject;
	}
}
