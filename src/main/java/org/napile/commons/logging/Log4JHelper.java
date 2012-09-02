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

package org.napile.commons.logging;

import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author VISTALL
 * @date 9:10/19.05.2011
 */
public class Log4JHelper
{
	public static void load()
	{
		try
		{
			URL url = Log4JHelper.class.getResource("log4j.xml");
			DOMConfigurator.configure(url);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}

		Logger logger = LogManager.getLogManager().getLogger("");
		for(Handler h : logger.getHandlers())
			logger.removeHandler(h);
	}
}
