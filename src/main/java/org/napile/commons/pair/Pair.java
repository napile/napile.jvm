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
