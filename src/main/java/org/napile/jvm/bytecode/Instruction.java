package org.napile.jvm.bytecode;

import java.nio.ByteBuffer;

import org.napile.jvm.vm.VmInterface;

/**
 * @author VISTALL
 * @date 10:16/05.02.2012
 */
public interface Instruction
{
	void parseData(ByteBuffer buffer);

	void call(VmInterface vmInterface);
}
