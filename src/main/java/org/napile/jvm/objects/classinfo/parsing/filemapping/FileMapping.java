package org.napile.jvm.objects.classinfo.parsing.filemapping;

import java.io.InputStream;

/**
 * @author VISTALL
 * @date 18:45/02.02.2012
 */
public interface FileMapping
{
	InputStream openSteam();

	String getName();
}
