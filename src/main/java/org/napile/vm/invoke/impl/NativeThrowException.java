package org.napile.vm.invoke.impl;

/**
 * @author VISTALL
 * @since 19:44/21.01.13
 */
public class NativeThrowException extends RuntimeException
{
	public final int index;

	public NativeThrowException(int index)
	{
		this.index = index;
	}
}
