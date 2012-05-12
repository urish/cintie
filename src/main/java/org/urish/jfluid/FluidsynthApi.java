package org.urish.jfluid;

import com.sun.jna.Library;

public interface FluidsynthApi extends Library {
	/*-- Settings --*/
	FluidSettings new_fluid_settings();

	void delete_fluid_settings(FluidSettings settings);

	/*-- Synth --*/
	Synth new_fluid_synth(FluidSettings settings);

	int fluid_synth_sfload(Synth synth, String fileName, boolean updateMidiPresets);

	public int fluid_synth_start(Synth synth);

	public int fluid_sequencer_get_tick(Synth synth);

	public int fluid_synth_program_select(Synth synth, int channel, int soundFont, int bankNum, int presetNum);

	public int fluid_synth_noteon(Synth synth, int channel, int key, int velocity);

	public int fluid_synth_noteoff(Synth synth, int channel, int key);

	public void delete_fluid_synth(Synth synth);

	/*-- Audio Drivers --*/
	AudioDriver new_fluid_audio_driver(FluidSettings settings, Synth synth);

	void delete_fluid_audio_driver(AudioDriver driver);

	/*-- Sequencers --*/
	FluidSequencer new_fluid_sequencer2(int use_system_timer);

	short fluid_sequencer_register_fluidsynth(FluidSequencer sequencer, Synth synth);

	int fluid_sequencer_send_at(FluidSequencer sequencer, FluidEvent event, int time, int absolute);

	void delete_fluid_sequencer(FluidSequencer sequencer);

	/*-- Events --*/
	public FluidEvent new_fluid_event();

	public void fluid_event_set_source(FluidEvent event, short source);

	public void fluid_event_set_dest(FluidEvent event, short dest);

	public int fluid_event_note(FluidEvent event, int channel, short key, short velocity, int duration);

	public int fluid_event_noteon(FluidEvent event, int channel, short key, short velocity);

	public int fluid_event_noteoff(FluidEvent event, int channel, short key);

	public void delete_fluid_event(FluidEvent event);
}
