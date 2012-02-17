package org.napile.vm.objects.classinfo.parsing.codeattributes;

/**
 * @author VISTALL
 * @date 19:12/17.02.2012
 */
public class ExceptionBlock
{
	public static final ExceptionBlock[] EMPTY_ARRAY = new ExceptionBlock[0];

	private int _startPc;
	private int _endPc;
	private int _handlePc;
	private int _catchType;

	public ExceptionBlock(int startPc, int endPc, int handlePc, int catchType)
	{
		_startPc = startPc;
		_endPc = endPc;
		_handlePc = handlePc;
		_catchType = catchType;
	}

	public int getStartPc()
	{
		return _startPc;
	}

	public int getEndPc()
	{
		return _endPc;
	}

	public int getHandlePc()
	{
		return _handlePc;
	}

	public int getCatchType()
	{
		return _catchType;
	}
}
