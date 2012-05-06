package org.urish.jnavst;

import com.sun.jna.Structure;

public class ERect extends Structure {
	public short top;
	public short left;
	public short bottom;
	public short right;

	@Override
	public String toString() {
		return "Rect[" + top + ", " + left + ", " + bottom + ", " + right + "]";
	}

	public class ByRef extends ERect implements ByReference {
	}
}
