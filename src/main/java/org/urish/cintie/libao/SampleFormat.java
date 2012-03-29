package org.urish.cintie.libao;

import com.sun.jna.Structure;

public class SampleFormat extends Structure {
	public int bits; /* bits per sample */
	public int rate; /* samples per second (in a single channel) */
	public int channels; /* number of audio channels */
	public int byte_format; /* Byte ordering in sample, see constants below */
	public String matrix; /* input channel location/ordering */
}
