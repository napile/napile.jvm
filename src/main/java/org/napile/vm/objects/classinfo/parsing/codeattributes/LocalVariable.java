package org.napile.vm.objects.classinfo.parsing.codeattributes;

import org.napile.vm.objects.classinfo.ClassInfo;

/**
 * @author VISTALL
 * @date 20:59/15.02.2012
 */
public class LocalVariable
{
	public static final LocalVariable[] EMPTY_ARRAY = new LocalVariable[0];
	private final int _startPc;
	private final int _length;
	private final String _name;
	private final ClassInfo _type;
	private final int _frameIndex;

	public LocalVariable(int startPc, int length, String name, ClassInfo type, int frameIndex)
	{
		_startPc = startPc;
		_length = length;
		_name = name;
		_type = type;
		_frameIndex = frameIndex;
	}

	public int getStartPc()
	{
		return _startPc;
	}

	public int getLength()
	{
		return _length;
	}

	public String getName()
	{
		return _name;
	}

	public ClassInfo getType()
	{
		return _type;
	}

	public int getFrameIndex()
	{
		return _frameIndex;
	}
}
