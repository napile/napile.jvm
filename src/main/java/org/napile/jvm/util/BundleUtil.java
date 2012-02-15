package org.napile.jvm.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.napile.jvm.Main;

/**
 * @author VISTALL
 * @date 20:25/02.02.2012
 */
public class BundleUtil
{
	private static final BundleUtil _instance = new BundleUtil();

	private ResourceBundle _bundle;

	public static BundleUtil getInstance()
	{
		return _instance;
	}

	private BundleUtil()
	{
		_bundle = ResourceBundle.getBundle("org/napile/jvm/util/Bundle", Locale.ENGLISH);
	}

	public static void exitAbnormal(Exception e, String value, Object... arg)
	{
		if(value != null)
			Main.LOGGER.error(getInstance().makeString(value, arg), e == null ? new Error() : e);

		System.exit(-1);
	}

	public String makeString(String key, Object... arg)
	{
		String text = _bundle.getString(key);
		if(arg.length > 0)
		{
			for(int i = 0; i < arg.length; i++)
				text = text.replace("{" + i + "}", String.valueOf(arg[i]));
		}

		return text;
	}
}
