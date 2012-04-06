package org.urish.cintie.engine
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Port
import javax.sound.sampled.DataLine
import java.io.File
import org.urish.cintie.libao.LibAO
import org.urish.cintie.libao.SampleFormat
import org.apache.commons.io.IOUtils
import org.apache.commons.io.FileUtils
import org.urish.cintie.util.LibraryLoader
import org.urish.openal.jna.ALFactory
import org.urish.openal.OpenAL

object Main {
  def main(args: Array[String]) {
    val engine = new CintieEngine()
    engine.start;
    Thread.sleep(1000000);
  }
}