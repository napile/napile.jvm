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

package org.napile.commons.pair;

/**
 * @author VISTALL
 * @date 23:39/15.02.2012
 */
public class Pair<K, V>
{
	private K _key;
	private V _value;

	public Pair(K key, V value)
	{
		_key = key;
		_value = value;
	}

	public K getKey()
	{
		return _key;
	}

	public void setKey(K key)
	{
		_key = key;
	}

	public V getValue()
	{
		return _value;
	}

	public void setValue(V value)
	{
		_value = value;
	}
}
