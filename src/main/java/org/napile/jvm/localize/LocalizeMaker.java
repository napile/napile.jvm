package org.napile.jvm.localize;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VISTALL
 * @date 20:25/02.02.2012
 */
public class LocalizeMaker
{
	private static final LocalizeMaker _instance = new LocalizeMaker();

	public final String[][] VALUES =
	{
		{"class.s1.not.found", "Class '{0}' not found."}
	};

	private Map<String, String> _values = new HashMap<String, String>(VALUES.length);

	public static LocalizeMaker getInstance()
	{
		return _instance;
	}

	private LocalizeMaker()
	{
		for(String[] entry : VALUES)
			_values.put(entry[0], entry[1]);
	}

	public String makeString(String key, Object... arg)
	{
		String text = _values.get(key);
		if(arg.length > 0)
		{
			for(int i = 0; i < arg.length; i++)
				text = text.replace("{" + i + "}", String.valueOf(arg[i]));
		}

		return text;
	}
}
