package org.urish.cintie.engine

import java.io.File

import scala.collection.mutable.ListBuffer

import org.urish.cintie.util.LibraryLoader
import org.urish.openal.jna.ALFactory
import org.urish.openal.OpenAL

object CintieEngine {
  val alfactory = new ALFactory();
}

class CintieEngine {
  val baseDir = new File(System.getProperty("cintie.soundDir"))
  LibraryLoader.loadEmbededLibrary("soft_oal.dll")
  val openAL = new OpenAL(CintieEngine.alfactory, null)
  var players: Seq[Player] = loadPlayers()
  var started = false

  def loadPlayers(): List[Player] = {
    val result = new ListBuffer[Player]

    for (file <- baseDir.listFiles()) {
      val iniFile = new File(file, "vst.ini")
      if (iniFile.exists()) {
        try {
          result += new VstPlayer(openAL, iniFile)
        } catch {
          case e => {
            e.printStackTrace()
            System.err.println("WARN: Could not initialize VST engine; VST pawn will not be available")
          }
        }
      } else {
        result += new FourSourcePlayer(openAL, file)
      }
    }
    System.out.println(result)
    return result.toList
  }

  def start {
    if (!started) {
      players.foreach(p => p.start)
      started = true
    }
  }

  def stop {
    if (started) {
      players.foreach(p => p.stop)
      started = false
    }
  }

  def player(id: Int) = players(id)

  def playerCount = players.length
}