/*
 * Copyright 2010-2012 napile.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.vm.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.napile.vm.Main;

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
		_bundle = ResourceBundle.getBundle("org/napile/vm/util/Bundle", Locale.ENGLISH);
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
