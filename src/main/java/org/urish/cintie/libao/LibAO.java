package org.urish.cintie.libao;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface LibAO extends Library {
	public final static int AO_FMT_LITTLE = 1;
	public final static int AO_FMT_BIG = 2;
	public final static int AO_FMT_NATIVE = 4;

	public final static LibAO instance = (LibAO) Native.loadLibrary("libao-4", LibAO.class);

	/* library setup/teardown */
	void ao_initialize();

	void ao_shutdown();

	int ao_default_driver_id();

	Pointer ao_open_live(int driver_id, SampleFormat sampleFormat, Pointer option);

	void ao_play(Pointer device, byte[] outputSamples, int numBytes);
	
	void ao_close(Pointer device);
}
