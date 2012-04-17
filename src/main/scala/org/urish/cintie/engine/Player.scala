package org.urish.cintie.engine
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl
import java.lang.Math
import com.synthbot.audioplugin.vst.vst2.JVstHost2
import javax.sound.midi.ShortMessage
import com.synthbot.audioio.vst.JVstAudioThread
import org.urish.cintie.util.LibraryLoader
import org.urish.cintie.util.VstPresetLoader
import org.urish.openal.OpenAL
import org.urish.openal.Tuple3F

abstract class Player {
  protected var _x: Float = 0;
  protected var _y: Float = 0;
  def x = _x
  def y = _y

  def start()
  def stop()
  def moveTo(x: Float, y: Float) = { this._x = x; this._y = y; }
}

class FourSourcePlayer(openAL: OpenAL, soundBank: File) extends Player {
  private var started = false;
  val clips = List(1, 2, 3, 4).map(i => new AudioClip(openAL, new File(soundBank, i + ".wav")))

  def start() {
    clips.foreach(clip => if (!clip.playing) { clip.start })
    started = true
    update
  }

  def stop() {
    started = false
    clips.foreach(clip => clip.stop)
  }

  def setGain(i: Int, value: Float) {
    clips(i - 1).volume_=(value)
  }

  def update {
    if (started) {
      setGain(1, x * y)
      setGain(2, x * (1 - y))
      setGain(3, (1 - x) * y)
      setGain(4, (1 - x) * (1 - y))
      val position = new Tuple3F((x - .5f) * 3, 0, (y - .5f) * 3);
      for (i <- 0 until 4)
        clips(i).position_=(position)
    }
  }

  override def moveTo(x: Float, y: Float) {
    super.moveTo(x, y)
    update
  }
}

class VstHarmonicPlayer(openAL: OpenAL, vstPath: File) extends Player with Runnable {
  var BLOCK_SIZE = 4096
  var SAMPLE_RATE = 44100

  LibraryLoader.loadEmbededLibrary("jvsthost2.dll")
  val vst = JVstHost2.newInstance(vstPath, SAMPLE_RATE, BLOCK_SIZE)
  VstPresetLoader.loadVstPreset(vst, new File(vstPath.getParent(), "nexus content/presets/Single Layer Leads/LD Synced.fxp"))
  val audioThread = new VSTClip(vst, openAL.createSource());
  var playing: Boolean = false;

  def start() = playing = true
  def stop() = playing = false

  val harmonic = List(0, 2, 4, 5, 7, 9, 11)
  var prevIndexXY = 0f

  val thread = new Thread(audioThread);
  thread.setDaemon(true)
  thread.start();

  val midiThread = new Thread(this)
  midiThread.setDaemon(true)
  midiThread.start()

  def run() {
    try {
      while (true) {
        val xy = this.x + this.y
        if (!playing || (xy == prevIndexXY)) {
          Thread.sleep(500)
        } else {
          prevIndexXY = xy
          val index = (this.y * 12).intValue()
          val note = (harmonic(index % 7) + 12 * (index / 7) + 60).asInstanceOf[Int];

          val channel = 0
          val velocity = (this.x * 37).intValue()
          val messageOn = new ShortMessage();
          messageOn.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
          vst.queueMidiMessage(messageOn);

          Thread.sleep(500);
          val messgeOff = new ShortMessage();
          messgeOff.setMessage(ShortMessage.NOTE_ON, channel, note, 0);
          vst.queueMidiMessage(messgeOff);
        }
      }
    }
  }
}