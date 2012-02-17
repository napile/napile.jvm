package org.napile.vm.objects.classinfo.parsing.codeattributes;

/**
 * @author VISTALL
 * @date 18:53/17.02.2012
 */
public class LineNumberEntry
{
	public static final LineNumberEntry[] EMPTY_ARRAY = new LineNumberEntry[0];
	private int _startPc;
	private int _lineNumber;

	public LineNumberEntry(int startPc, int lineNumber)
	{
		_startPc = startPc;
		_lineNumber = lineNumber;
	}

	public int getStartPc()
	{
		return _startPc;
	}

	public int getLineNumber()
	{
		return _lineNumber;
	}
}
