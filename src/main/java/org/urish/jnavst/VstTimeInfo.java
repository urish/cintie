package org.urish.jnavst;

import com.sun.jna.Structure;

public class VstTimeInfo extends Structure {
	public double samplePos; // /< current Position in audio samples (always valid)
	public double sampleRate; // /< current Sample Rate in Herz (always valid)
	public double nanoSeconds; // /< System Time in nanoseconds (10^-9 second)
	public double ppqPos; // /< Musical Position, in Quarter Note (1.0 equals 1 Quarter
					// Note)
	public double tempo; // /< current Tempo in BPM (Beats Per Minute)
	public double barStartPos; // /< last Bar Start Position, in Quarter Note
	public double cycleStartPos; // /< Cycle Start (left locator), in Quarter Note
	public double cycleEndPos; // /< Cycle End (right locator), in Quarter Note
	public int timeSigNumerator; // /< Time Signature Numerator (e.g. 3 for 3/4)
	public int timeSigDenominator; // /< Time Signature Denominator (e.g. 4 for 3/4)
	public int smpteOffset; // /< SMPTE offset (in SMPTE subframes (bits; 1/80 of a
						// frame)). The current SMPTE position can be calculated
						// using #samplePos, #sampleRate, and #smpteFrameRate.
	public int smpteFrameRate; // /< @see VstSmpteFrameRate
	public int samplesToNextClock; // /< MIDI Clock Resolution (24 Per Quarter Note),
							// can be negative (nearest clock)
	public int flags; // /< @see VstTimeInfoFlags
}
