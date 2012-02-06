/*
 * Copyright (c) 1999, 2005, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.tools.javac.jvm;


/**
 * Bytecode instruction codes, as well as typecodes used as
 * instruction modifiers.
 * <p/>
 * <p><b>This is NOT part of any supported API.
 * If you write code that depends on this, you do so at your own risk.
 * This code and its internal interfaces are subject to change or
 * deletion without notice.</b>
 */
public interface ByteCodes
{
	int illegal = -1;
	int nop = 0;
	int aconst_null = 1;
	int iconst_m1 = 2;
	int iconst_0 = 3;
	int iconst_1 = 4;
	int iconst_2 = 5;
	int iconst_3 = 6;
	int iconst_4 = 7;
	int iconst_5 = 8;
	int lconst_0 = 9;
	int lconst_1 = 10;
	int fconst_0 = 11;
	int fconst_1 = 12;
	int fconst_2 = 13;
	int dconst_0 = 14;
	int dconst_1 = 15;
	int bipush = 16;
	int sipush = 17;
	int ldc1 = 0x12;
	int ldc2 = 19;
	int ldc2w = 20;
	int iload = 21;
	int lload = 22;
	int fload = 23;
	int dload = 24;
	int aload = 25;
	int iload_0 = 26;
	int iload_1 = 27;
	int iload_2 = 28;
	int iload_3 = 29;
	int lload_0 = 30;
	int lload_1 = 31;
	int lload_2 = 32;
	int lload_3 = 33;
	int fload_0 = 34;
	int fload_1 = 35;
	int fload_2 = 36;
	int fload_3 = 37;
	int dload_0 = 38;
	int dload_1 = 39;
	int dload_2 = 40;
	int dload_3 = 41;
	int aload_0 = 42;
	int aload_1 = 43;
	int aload_2 = 44;
	int aload_3 = 45;
	int iaload = 46;
	int laload = 47;
	int faload = 48;
	int daload = 49;
	int aaload = 50;
	int baload = 51;
	int caload = 52;
	int saload = 53;
	int istore = 54;
	int lstore = 55;
	int fstore = 56;
	int dstore = 57;
	int astore = 58;
	int istore_0 = 59;
	int istore_1 = 60;
	int istore_2 = 61;
	int istore_3 = 62;
	int lstore_0 = 63;
	int lstore_1 = 64;
	int lstore_2 = 65;
	int lstore_3 = 66;
	int fstore_0 = 67;
	int fstore_1 = 68;
	int fstore_2 = 69;
	int fstore_3 = 70;
	int dstore_0 = 71;
	int dstore_1 = 72;
	int dstore_2 = 73;
	int dstore_3 = 74;
	int astore_0 = 75;
	int astore_1 = 76;
	int astore_2 = 77;
	int astore_3 = 78;
	int iastore = 79;
	int lastore = 80;
	int fastore = 81;
	int dastore = 82;
	int aastore = 83;
	int bastore = 84;
	int castore = 85;
	int sastore = 86;
	int pop = 87;
	int pop2 = 88;
	int dup = 89;
	int dup_x1 = 90;
	int dup_x2 = 91;
	int dup2 = 92;
	int dup2_x1 = 93;
	int dup2_x2 = 94;
	int swap = 95;
	int iadd = 96;
	int ladd = 97;
	int fadd = 98;
	int dadd = 99;
	int isub = 100;
	int lsub = 101;
	int fsub = 102;
	int dsub = 103;
	int imul = 104;
	int lmul = 105;
	int fmul = 106;
	int dmul = 107;
	int idiv = 108;
	int ldiv = 109;
	int fdiv = 110;
	int ddiv = 111;
	int imod = 112;
	int lmod = 113;
	int fmod = 114;
	int dmod = 115;
	int ineg = 116;
	int lneg = 117;
	int fneg = 118;
	int dneg = 119;
	int ishl = 120;
	int lshl = 121;
	int ishr = 122;
	int lshr = 123;
	int iushr = 124;
	int lushr = 125;
	int iand = 126;
	int land = 127;
	int ior = 128;
	int lor = 129;
	int ixor = 130;
	int lxor = 131;
	int iinc = 132;
	int i2l = 133;
	int i2f = 134;
	int i2d = 135;
	int l2i = 136;
	int l2f = 137;
	int l2d = 138;
	int f2i = 139;
	int f2l = 140;
	int f2d = 141;
	int d2i = 142;
	int d2l = 143;
	int d2f = 144;
	int int2byte = 145;
	int int2char = 146;
	int int2short = 147;
	int lcmp = 148;
	int fcmpl = 149;
	int fcmpg = 150;
	int dcmpl = 151;
	int dcmpg = 152;
	int ifeq = 153;
	int ifne = 154;
	int iflt = 155;
	int ifge = 156;
	int ifgt = 157;
	int ifle = 158;
	int if_icmpeq = 159;
	int if_icmpne = 160;
	int if_icmplt = 161;
	int if_icmpge = 162;
	int if_icmpgt = 163;
	int if_icmple = 164;
	int if_acmpeq = 165;
	int if_acmpne = 166;
	int goto_ = 167;
	int jsr = 168;
	int ret = 169;
	int tableswitch = 170;
	int lookupswitch = 171;
	int ireturn = 172;
	int lreturn = 173;
	int freturn = 174;
	int dreturn = 175;
	int areturn = 176;
	int return_ = 177;
	int getstatic = 178;
	int putstatic = 179;
	int getfield = 180;
	int putfield = 181;
	int invokevirtual = 182;
	int invokespecial = 183;
	int invokestatic = 184;
	int invokeinterface = 185;
	int invokedynamic = 186;
	int new_ = 187;
	int newarray = 188;
	int anewarray = 189;
	int arraylength = 190;
	int athrow = 191;
	int checkcast = 192;
	int instanceof_ = 193;
	int monitorenter = 194;
	int monitorexit = 195;
	int wide = 196;
	int multianewarray = 197;
	int if_acmp_null = 198;
	int if_acmp_nonnull = 199;
	int goto_w = 200;
	int jsr_w = 201;
	int breakpoint = 202;
	int ByteCodeCount = 203;

	/**
	 * Virtual instruction codes; used for constant folding.
	 */
	int string_add = 256,  // string +
			bool_not = 257,  // boolean !
			bool_and = 258,  // boolean &&
			bool_or = 259;  // boolean ||

	/**
	 * Virtual opcodes; used for shifts with long shiftcount
	 */
	int ishll = 270,  // int shift left with long count
			lshll = 271,  // long shift left with long count
			ishrl = 272,  // int shift right with long count
			lshrl = 273,  // long shift right with long count
			iushrl = 274,  // int unsigned shift right with long count
			lushrl = 275;  // long unsigned shift right with long count

	/**
	 * Virtual opcode for null reference checks
	 */
	int nullchk = 276;  // return operand if non-null,
	// otherwise throw NullPointerException.

	/**
	 * Virtual opcode for disallowed operations.
	 */
	int error = 277;

	/**
	 * All conditional jumps come in pairs. To streamline the
	 * treatment of jumps, we also introduce a negation of an
	 * unconditional jump. That opcode happens to be jsr.
	 */
	int dontgoto = jsr;

	/**
	 * Shift and mask constants for shifting prefix instructions.
	 * a pair of instruction codes such as LCMP ; IFEQ is encoded
	 * in Symtab as (LCMP << preShift) + IFEQ.
	 */
	int preShift = 9;
	int preMask = (1 << preShift) - 1;

	/**
	 * Type codes.
	 */
	int INTcode = 0, LONGcode = 1, FLOATcode = 2, DOUBLEcode = 3, OBJECTcode = 4, BYTEcode = 5, CHARcode = 6, SHORTcode = 7, VOIDcode = 8, TypeCodeCount = 9;

	static final String[] typecodeNames = {"int", "long", "float", "double", "object", "byte", "char", "short", "void", "oops"};
}
