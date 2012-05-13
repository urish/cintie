package org.urish.jfluid;

import com.sun.jna.Library;

public interface FluidsynthApi extends Library {
	/*-- Settings --*/
	public FluidSettings new_fluid_settings();

	public void delete_fluid_settings(FluidSettings settings);

	/*-- Synth --*/
	public Synth new_fluid_synth(FluidSettings settings);

	public int fluid_synth_sfload(Synth synth, String fileName, boolean updateMidiPresets);

	public int fluid_synth_start(Synth synth);

	public int fluid_synth_program_select(Synth synth, int channel, int soundFont, int bankNum, int presetNum);

	public int fluid_synth_noteon(Synth synth, int channel, int key, int velocity);

	public int fluid_synth_noteoff(Synth synth, int channel, int key);

	public int fluid_synth_write_s16(Synth synth, int length, short[] leftBuf, int leftOffset, int leftIncr, short[] rightBuf,
			int rightOffset, int rightIncr);

	public int fluid_synth_write_s16(Synth synth, int lengthInSamples, byte[] leftBufBytes, int leftOffset, int leftIncr,
			byte[] rightBufBytes, int rightOffset, int rightIncr);

	public void delete_fluid_synth(Synth synth);

	/*-- Audio Drivers --*/
	public AudioDriver new_fluid_audio_driver(FluidSettings settings, Synth synth);

	public void delete_fluid_audio_driver(AudioDriver driver);

	/*-- Sequencers --*/
	public FluidSequencer new_fluid_sequencer();

	public FluidSequencer new_fluid_sequencer2(boolean use_system_timer);

	public short fluid_sequencer_register_fluidsynth(FluidSequencer sequencer, Synth synth);

	public int fluid_sequencer_get_tick(FluidSequencer sequencer);

	public int fluid_sequencer_send_now(FluidSequencer sequencer, FluidEvent event);

	public int fluid_sequencer_send_at(FluidSequencer sequencer, FluidEvent event, int time, boolean absolute);

	public void delete_fluid_sequencer(FluidSequencer sequencer);

	/*-- Events --*/
	public FluidEvent new_fluid_event();

	public void fluid_event_set_source(FluidEvent event, short source);

	public void fluid_event_set_dest(FluidEvent event, short dest);

	public int fluid_event_note(FluidEvent event, int channel, short key, short velocity, int duration);

	public int fluid_event_noteon(FluidEvent event, int channel, short key, short velocity);

	public int fluid_event_noteoff(FluidEvent event, int channel, short key);

	public void delete_fluid_event(FluidEvent event);
}
