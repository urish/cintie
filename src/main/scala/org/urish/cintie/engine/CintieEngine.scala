package org.urish.cintie.engine
import java.io.File
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl

class CintieEngine {
  val baseDir = new File("""C:\projects\FreshPaint\Assaf""")

  var players: List[Player] = List(new FourSourcePlayer(new File(baseDir, "gtr")), new FourSourcePlayer(new File(baseDir, "piano")),
    new FourSourcePlayer(new File(baseDir, "drum")))

  def start {
  }
  
  def stop {
  }
  
  def player(id: Int) = players(id)
}