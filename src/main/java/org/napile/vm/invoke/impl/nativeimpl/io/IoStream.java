package org.napile.vm.invoke.impl.nativeimpl.io;

import java.io.Closeable;

/**
 * @author VISTALL
 * @since 13:37/15.01.13
 */
public abstract class IoStream<T extends Closeable>
{
	public final T stream;

	protected IoStream(T stream)
	{
		this.stream = stream;
	}

	protected abstract boolean writeOrRead(byte[] bytes, int offset, int length);
}
