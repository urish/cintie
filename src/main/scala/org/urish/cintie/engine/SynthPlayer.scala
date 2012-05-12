package org.urish.cintie.engine
import org.urish.openal.OpenAL
import org.urish.jfluid.FluidSettings
import javax.sound.sampled.AudioFormat
import org.urish.jfluid.FluidsynthApi
import org.urish.jfluid.Synth
import java.io.OutputStream
import org.urish.openal.Source
import com.sun.jna.Native

class SynthPlayer extends Player {

  val fluidsynth = Native.loadLibrary("fluidsynth", classOf[FluidsynthApi]).asInstanceOf[FluidsynthApi]
  val settings = fluidsynth.new_fluid_settings();
  val synth = fluidsynth.new_fluid_synth(settings);
  val soundFontId = fluidsynth.fluid_synth_sfload(synth, "c:/p/cinto/media/FluidR3 GM.sf2", false);
  fluidsynth.fluid_synth_program_select(synth, 0, soundFontId, 0, 48);
  val audioFormat = new AudioFormat(44100, 16, 1, true, false);

  def start(): Unit = {}

  def stop(): Unit = {}

}