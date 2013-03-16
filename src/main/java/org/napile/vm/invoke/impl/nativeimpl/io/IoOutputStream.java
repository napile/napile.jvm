package org.napile.vm.invoke.impl.nativeimpl.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author VISTALL
 * @since 13:37/15.01.13
 */
public class IoOutputStream extends IoStream<OutputStream>
{
	public IoOutputStream(OutputStream stream)
	{
		super(stream);
	}

	@Override
	protected boolean writeOrRead(byte[] bytes, int offset, int length)
	{
		try
		{
			stream.write(bytes, offset, length);
			return true;
		}
		catch(IOException e)
		{
			return false;
		}
	}
}
