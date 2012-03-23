package org.urish.cintie.engine
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl
import java.lang.Math
import com.synthbot.audioplugin.vst.vst2.JVstHost2
import javax.sound.midi.ShortMessage
import com.synthbot.audioio.vst.JVstAudioThread
import org.urish.cintie.util.Library
import org.urish.cintie.util.VstPresetLoader

class Player {
  var x: Float = 0;
  var y: Float = 0;
}

class FourSourcePlayer(soundBank: File) extends Player {
  val clips = List(1, 2, 3, 4).map(i => new AudioClip(new File(soundBank, i + ".wav")))
  clips.map(clip => clip.start())
  update

  def setGain(i: Int, value: Float) {
    clips(i - 1).setVolume(value)
  }

  def update {
    setGain(1, x * y)
    setGain(2, x * (1 - y))
    setGain(3, (1 - x) * y)
    setGain(4, (1 - x) * (1 - y))
  }
}

class VstHarmonicPlayer(vstPath: File) extends Player with Runnable {
  var BLOCK_SIZE = 4096
  var SAMPLE_RATE = 44100

  Library.loadEmbededLibrary("jvsthost2.dll")
  val vst = JVstHost2.newInstance(vstPath, SAMPLE_RATE, BLOCK_SIZE)
  VstPresetLoader.loadVstPreset(vst, new File(vstPath.getParent(), "nexus content/presets/piano/PN Nexus Grandpiano.fxp"))
  val audioThread = new JVstAudioThread(vst);

  val thread = new Thread(audioThread);
  thread.setDaemon(true)
  thread.start();
  new Thread(this).start()

  def run() {
    try {
      while (true) {
        val note = (((this.x + this.y) * 24) + 48).asInstanceOf[Int];

        val channel = 0
        val velocity = 127
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