package org.urish.cintie.engine
import java.io.File
import java.lang.Math
import com.synthbot.audioio.vst.JVstAudioThread
import com.synthbot.audioplugin.vst.vst2.JVstHost2
import com.synthbot.audioplugin.vst.vst2.JVstPersistence
import javax.sound.midi.ShortMessage
import org.urish.cintie.util.VstPresetLoader

object Main {
  def main(args: Array[String]) {
    val SAMPLE_RATE = 44100;
    val BLOCK_SIZE = 4096;

    val vst = JVstHost2.newInstance(new File("""C:\Program Files (x86)\VstPlugins\Nexus.dll"""), SAMPLE_RATE, BLOCK_SIZE);
    VstPresetLoader.loadVstPreset(vst, new File("""C:\Program Files (x86)\VstPlugins\Nexus Content\Presets\Voice\VO Arms of Heaven.fxp"""))
//    val vst = JVstHost2.newInstance(new File("""C:\Program Files (x86)\VstPlugins\LennarDigital\Sylenth1\Sylenth1.dll"""), SAMPLE_RATE, BLOCK_SIZE);
    val audioThread = new JVstAudioThread(vst);
    val thread = new Thread(audioThread);
    thread.setDaemon(true)
    thread.start();
    new Thread(new Runnable() {
      def run() {
        try {
          while (true) {
            val note = ((Math.random() * 24) + 48).asInstanceOf[Int];

            val channel = 0
            val velocity = 25
            val messageOn = new ShortMessage();
            messageOn.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
            vst.queueMidiMessage(messageOn);

            Thread.sleep(1000);
            val messgeOff = new ShortMessage();
            messgeOff.setMessage(ShortMessage.NOTE_ON, channel, note, 0);
            vst.queueMidiMessage(messgeOff);
          }
        }
      }
    }).start()
    //val engine = new CintieEngine()
    //engine.start;
    Thread.sleep(1000000);
  }
}