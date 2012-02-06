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
		{"class.s1.not.found", "Class '{0}' not found."},
		{"invalid.constant.value.class.s1", "Invalid constant value. Class '{0}'"} ,
		{"invalid.attribute.class.s1", "Invalid attribute '{0}'. Class '{1}'"},
		{"not.found.s1.s2.s3", "Not found static method by '{0}.{1}({2})'"},
		{"error.in.code.pre.code.s1.file.s2.position.s3.method.s4", "Error in bytecode. Prev. bytecode {0}. File: {1}. Position: {2}. Method {3}"}
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
		if(text == null)
			return "Not find key '" + key + "'";
		if(arg.length > 0)
		{
			for(int i = 0; i < arg.length; i++)
				text = text.replace("{" + i + "}", String.valueOf(arg[i]));
		}

		return text;
	}
}
