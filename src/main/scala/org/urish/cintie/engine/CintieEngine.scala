package org.urish.cintie.engine
import java.io.File
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl

class CintieEngine {
  val baseDir = new File("""C:\projects\FreshPaint\Assaf""")
  val vstPath = new File("""C:\Program Files (x86)\VstPlugins""")

  var players: List[Player] = List(new FourSourcePlayer(new File(baseDir, "gtr")), new FourSourcePlayer(new File(baseDir, "piano")),
    new FourSourcePlayer(new File(baseDir, "drum")), new VstHarmonicPlayer(new File(vstPath, "nexus.dll")))

  def start {
  }
  
  def stop {
  }
  
  def player(id: Int) = players(id)
}