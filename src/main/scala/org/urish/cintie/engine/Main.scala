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
    val engine = new CintieEngine()
    engine.start;
    Thread.sleep(1000000);
  }
}