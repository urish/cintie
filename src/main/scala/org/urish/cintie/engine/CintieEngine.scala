package org.urish.cintie.engine
import java.io.File
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import scala.collection.mutable.ListBuffer

class CintieEngine {
  val baseDir = new File(System.getProperty("cintie.soundDir"))
  val vstPath = new File("""C:\Program Files (x86)\VstPlugins""")

  var players: ListBuffer[Player] = ListBuffer(new FourSourcePlayer(new File(baseDir, "gtr")), new FourSourcePlayer(new File(baseDir, "piano")),
    new FourSourcePlayer(new File(baseDir, "drum")))

  try {
    players += new VstHarmonicPlayer(new File(vstPath, "nexus.dll"))
  } catch {
    case e => {
      e.printStackTrace()
      System.err.println("WARN: Could not initialize VST engine; VST pawn will not be available")
    }
  }

  def start {
    players.foreach(p => p.start)
  }

  def stop {
    players.foreach(p => p.stop)
  }

  def player(id: Int) = players(id)
}