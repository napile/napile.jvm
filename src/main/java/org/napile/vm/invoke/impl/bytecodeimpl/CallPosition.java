/*
 * Copyright 2010-2013 napile.org
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

package org.napile.vm.invoke.impl.bytecodeimpl;

import org.napile.asm.tree.members.bytecode.InstructionInCodePosition;
import org.napile.vm.invoke.impl.bytecodeimpl.bytecode.VmInstruction;
import org.napile.vm.objects.classinfo.MethodInfo;

/**
 * @author VISTALL
 * @date 14:26/15.02.13
 */
public class CallPosition
{
	public static final CallPosition EMPTY = new CallPosition(null);

	public final String className;
	public final String methodName;
	public final String fileName;
	public final int line;
	public final int column;

	public CallPosition(VmInstruction<?> instruction)
	{
		MethodInfo methodInfo = instruction == null ? null : instruction.parent;
		if(methodInfo != null)
		{
			className = methodInfo.getParent().getFqName().getFqName();
			methodName = methodInfo.getName();
		}
		else
		{
			className = "unknown class";
			methodName = "unknown method";
		}

		InstructionInCodePosition position = instruction == null ? null : instruction.instruction.position;

		fileName = position != null ? position.getFile() : "unknown source";
		line = position != null ? position.getLine() : -1;
		column = position != null ? position.getColumn() : -1;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("CallInfo{");
		sb.append("className='").append(className).append('\'');
		sb.append(", methodName='").append(methodName).append('\'');
		sb.append(", fileName='").append(fileName).append('\'');
		sb.append(", line=").append(line);
		sb.append(", column=").append(column);
		sb.append('}');
		return sb.toString();
	}
}
