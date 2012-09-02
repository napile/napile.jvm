package org.napile.vm.objects.classinfo;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.napile.compiler.lang.resolve.name.FqName;
import org.napile.vm.objects.objectinfo.ObjectInfo;

/**
 * @author VISTALL
 * @date 16:01/31.01.2012
 */
public interface ClassInfo extends ReflectInfo
{
	public static final int MAGIC_HEADER = 0xCAFEBABE;

	public static final ClassInfo[] EMPTY_ARRAY = new ClassInfo[0];

	@NotNull
	public FqName getName();

	@NotNull
	public List<FieldInfo> getFields();

	@NotNull
	public List<MethodInfo> getMethods();

	@NotNull
	public List<ClassInfo> getExtends();

	public ObjectInfo nullValue();

	boolean isStaticConstructorCalled();

	void setStaticConstructorCalled(boolean staticInit);
}
