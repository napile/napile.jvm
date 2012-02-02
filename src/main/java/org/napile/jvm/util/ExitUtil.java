package org.napile.jvm.util;

import org.apache.log4j.Logger;
import org.napile.jvm.localize.LocalizeMaker;

/**
 * @author VISTALL
 * @date 16:31/31.01.2012
 */
public class ExitUtil
{
	private static final Logger LOGGER = Logger.getLogger(ExitUtil.class);

	public static void exitAbnormal(String value, Object... arg)
	{
		if(value != null)
			LOGGER.error(LocalizeMaker.getInstance().makeString(value, arg));

		System.exit(-1);
	}
}
