package org.urish.cintie.engine
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl
import java.lang.Math

class Player() {
  var x: Float = 0;
  var y: Float = 0;
}

class FourSourcePlayer(soundBank: File) extends Player {
  val clips = List(1, 2, 3, 4).map(i => new AudioClip(new File(soundBank, i + ".wav")))
  clips.map(clip => clip.start())
  update

  def setGain(i: Int, value: Float) {
    clips(i - 1).setVolume(value)
  }

  def update {
    setGain(1, x * y)
    setGain(2, x * (1 - y))
    setGain(3, (1 - x) * y)
    setGain(4, (1 - x) * (1 - y))
  }
}