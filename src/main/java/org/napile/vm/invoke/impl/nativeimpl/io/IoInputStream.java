package org.napile.vm.invoke.impl.nativeimpl.io;

import java.io.InputStream;

/**
 * @author VISTALL
 * @date 13:41/15.01.13
 */
public class IoInputStream extends IoStream<InputStream>
{
	public IoInputStream(InputStream stream)
	{
		super(stream);
	}

	@Override
	protected boolean writeOrRead(byte[] bytes, int offset, int length)
	{
		return false;
	}
}
