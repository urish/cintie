package org.urish.cintie.engine

import java.io.File

import scala.collection.mutable.ListBuffer

import org.urish.openal.OpenAL

class CintieEngine {
  val baseDir = new File(System.getProperty("cintie.soundDir"))
  val vstPath = findVstPath()
  val openAL = new OpenAL()
  var players: Seq[Player] = loadPlayers()
  
  def findVstPath(): File = {
    val cand1 = new File("""C:\Program Files (x86)\VstPlugins""")
    if (cand1.exists()) {
      return cand1
    }
    val cand2 = new File("""C:\Program Files\Cakewalk\VstPlugins""")
    return cand2
  }

  def loadPlayers(): List[Player] = {
    val result = new ListBuffer[Player]

    for (file <- baseDir.listFiles()) {
      result += new FourSourcePlayer(openAL, file)
    }

    try {
      result += new NexusVstPlayer(openAL, new File(vstPath, "nexus.dll"))
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