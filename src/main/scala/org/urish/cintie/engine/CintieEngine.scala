package org.urish.cintie.engine

import java.io.File
import scala.collection.mutable.ListBuffer
import org.urish.openal.OpenAL
import org.urish.cintie.util.LibraryLoader

class CintieEngine {
  val baseDir = new File(System.getProperty("cintie.soundDir"))
  val vstPath = new File("""C:\Program Files (x86)\VstPlugins""")
  val openAL = new OpenAL()
  var players: Seq[Player] = loadPlayers()

  def loadPlayers(): List[Player] = {
    val result = new ListBuffer[Player]

    for (file <- baseDir.listFiles()) {
      result += new FourSourcePlayer(openAL, file)
    }

    try {
      result += new VstHarmonicPlayer(openAL, new File(vstPath, "nexus.dll"))
    } catch {
      case e => {
        e.printStackTrace()
        System.err.println("WARN: Could not initialize VST engine; VST pawn will not be available")
      }
    }
    System.out.println(result)
    return result.toList
  }

  def start {
    players.foreach(p => p.start)
  }

  def stop {
    players.foreach(p => p.stop)
  }

  def player(id: Int) = players(id)

  def playerCount = players.length
}