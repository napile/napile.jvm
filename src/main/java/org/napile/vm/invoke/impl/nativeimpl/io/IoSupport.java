package org.napile.vm.invoke.impl.nativeimpl.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.napile.vm.invoke.impl.bytecodeimpl.InterpreterContext;
import org.napile.vm.objects.BaseObjectInfo;
import org.napile.vm.vm.Vm;
import org.napile.vm.vm.VmUtil;

/**
 * @author VISTALL
 * @since 13:25/15.01.13
 */
public class IoSupport
{
	public static final IoSupport INSTANCE = new IoSupport();

	private Map<Long, IoStream> streams = new HashMap<Long, IoStream>();
	private long index;

	public long register(OutputStream stream)
	{
		long id = ++ index;
		streams.put(id, new IoOutputStream(stream));
		return id;
	}

	public long register(InputStream stream)
	{
		long id = ++ index;
		streams.put(id, new IoInputStream(stream));
		return id;
	}

	public BaseObjectInfo writeOrRead(Vm vm, InterpreterContext context, BaseObjectInfo[] arguments)
	{
		Long desc = VmUtil.convertToJava(vm, arguments[0]);

		IoStream stream = streams.get(desc);
		if(stream == null)
			return VmUtil.convertToVm(vm, context, -1L);

		byte[] bytes = VmUtil.convertToJava(vm, arguments[1]);
		int offset = (Integer) VmUtil.convertToJava(vm, arguments[2]);
		int length = (Integer) VmUtil.convertToJava(vm, arguments[3]);
		boolean result = stream.writeOrRead(bytes, offset, length);
		return VmUtil.convertToVm(vm, context, result ? desc : -1L);
	}
}
