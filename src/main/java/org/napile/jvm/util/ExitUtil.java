package org.napile.jvm.util;

import org.apache.log4j.Logger;

/**
 * @author VISTALL
 * @date 16:31/31.01.2012
 */
public class ExitUtil
{
	private static final Logger LOGGER = Logger.getLogger(ExitUtil.class);

	public static void exitAbnormal(String text)
	{
		if(text != null)
			LOGGER.error(text);

		System.exit(-1);
	}
}
