package org.urish.cintie.engine
import org.urish.jfluid.FluidsynthApi
import org.urish.openal.OpenAL

import com.sun.jna.Native

class SynthPlayer(val openAL: OpenAL) extends Player {
  val fluidsynth = Native.loadLibrary("fluidsynth", classOf[FluidsynthApi]).asInstanceOf[FluidsynthApi]
  val settings = fluidsynth.new_fluid_settings();
  val synth = fluidsynth.new_fluid_synth(settings);
  val soundFontId = fluidsynth.fluid_synth_sfload(synth, "media/FluidR3 GM.sf2", false);
  fluidsynth.fluid_synth_program_select(synth, 0, soundFontId, 0, 1);

  var synthThread: SynthThread = null;
  var harmonic = Array[Int](0, 2, 3, 5, 7, 10)
  var basePitch = 69
  private var _preset = 1

  val source = openAL.createSource()
  var nextBuffer = 0

  def start(): Unit = {
    if (synthThread == null) {
      synthThread = new SynthThread(openAL, source, fluidsynth, synth)
      synthThread.start()
    }
  }

  def stop(): Unit = {
    if (synthThread != null) {
      synthThread.stop();
      synthThread = null;
    }
  }
  
  def preset = _preset
  def preset_=(preset: Int) {
    fluidsynth.fluid_synth_program_select(synth, 0, soundFontId, 0, preset);
    _preset = preset
  }

  def square(x: Float): Float = { x * x }

  var lastX = -1f
  var lastY = -1f
  var lastTick = 0
  override def moveTo(x: Float, y: Float) {
    super.moveTo(x, y)
    val delta = Math.sqrt(square(x - lastX) + square(y - lastY))
    if (delta < 0.05) {
      return
    }
    if (synthThread.currentTick() - lastTick < 250) {
      return
    }
    lastTick = synthThread.currentTick()
    lastX = x
    lastY = y
    val xf = x * 8
    val yf = y * 8
    val note = (xf + yf).intValue()
    val octave = (note / harmonic.length) % 2
    synthThread.sendNote(0, (basePitch + octave * 12 + harmonic(note % harmonic.length)).shortValue(), 127, 220)
  }

  override def setMute(mute: Boolean) {
    source.setGain(if (mute) 0.001f else 1)
  }
}