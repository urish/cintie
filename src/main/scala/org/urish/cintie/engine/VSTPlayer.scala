package org.urish.cintie.engine
import java.io.File
import org.urish.cintie.util.LibraryLoader
import org.urish.openal.OpenAL
import com.synthbot.audioplugin.vst.vst2.JVstHost2
import javax.sound.midi.ShortMessage
import com.synthbot.audioplugin.vst.vst2.JVstPersistence

object NexusControlMapping {
  val mstFlt = 0
  val mstCut = 1
  val revMix = 8
  val output = 13
}

class NexusVstPlayer(openAL: OpenAL, vstPath: File) extends Player with Runnable {
  var BLOCK_SIZE = 4096
  var SAMPLE_RATE = 44100

  LibraryLoader.loadEmbededLibrary("jvsthost2.dll")
  val vst = JVstHost2.newInstance(vstPath, SAMPLE_RATE, BLOCK_SIZE)
  JVstPersistence.ignorePluginVersion = true
  JVstPersistence.loadPreset(vst, new File(vstPath.getParent(), "nexus content/presets/Voice/VO Amigavoice Soft.fxp"))
  vst.turnOn()
  val audioThread = new VSTClip(vst, openAL.createSource());
  var playing: Boolean = false;

  def start() = playing = true
  def stop() = playing = false

  val harmonic = List(0, 2, 3, 5, 7, 10)
  var prevIndexXY = 0f

  val thread = new Thread(audioThread);
  thread.setDaemon(true)
  thread.start();

  val midiThread = new Thread(this)
  midiThread.setDaemon(true)
  midiThread.start()

  vst.setParameter(NexusControlMapping.mstFlt, 1)
  vst.setParameter(NexusControlMapping.revMix, 80 / 127f)
  vst.setParameter(NexusControlMapping.output, 0.2f)

  def run() {
    try {
      while (true) {
        val xy = this.x + this.y
        if (!playing || (xy == prevIndexXY) || mute) {
          Thread.sleep(500)
        } else {
          prevIndexXY = xy
          val index = (this.y * 11).intValue()
          val note = (harmonic(index % harmonic.length) + 12 * (index / harmonic.length) + 69).asInstanceOf[Int];

          vst.setParameter(NexusControlMapping.mstCut, 0.4f + this.x * 0.6f);

          val channel = 0
          val velocity = 127;
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