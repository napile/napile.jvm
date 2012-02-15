package org.napile.vm.util;

/**
 * @author VISTALL
 * @date 8:26/05.02.2012
 */
public class StringCharReader
{
	private char[] _data;
	private int _index;

	public StringCharReader(String data)
	{
		_data = data.toCharArray();
	}

	public char next()
	{
		return _data[_index ++];
	}

	public char back()
	{
		return _data[--_index] ;
	}

	public char current()
	{
		return _index == 0 ? _data[_index] : _data[_index - 1];
	}

	public int index()
	{
		return _index;
	}

	public boolean hasNext()
	{
		return _index < _data.length;
	}

	@Override
	public String toString()
	{
		return new String(_data);
	}
}
