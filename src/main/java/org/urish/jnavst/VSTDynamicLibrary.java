package org.urish.jnavst;

import com.sun.jna.Library;

public interface VSTDynamicLibrary extends Library {
	public AEffect VSTPluginMain(HostCallback hostCallback);

	public AEffect main(HostCallback hostCallback);
}
