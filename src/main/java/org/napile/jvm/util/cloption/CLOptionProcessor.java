package org.napile.jvm.util.cloption;

import org.napile.jvm.vm.VmContext;

/**
 * @author VISTALL
 * @date 16:27/31.01.2012
 */
public interface CLOptionProcessor
{
	void process(VmContext context, String value);
}
