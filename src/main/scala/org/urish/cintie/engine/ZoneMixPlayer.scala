package org.urish.cintie.engine

import org.urish.openal.OpenAL
import org.urish.openal.Tuple3F
import java.io.File

class ZoneMixPlayer(openAL: OpenAL, soundBank: File) extends Player {

  class Point(val x: Float, val y: Float) {
  }

  private var started = false;
  val clips = loadClips()
  val backgroundClipFile = new File(soundBank, "background.wav")
  val backgroundClip = if (backgroundClipFile.exists()) new AudioClip(openAL, backgroundClipFile) else null;

  def start() {
    clips.foreach(clip => if (!clip.playing) { clip.start })
    if (backgroundClip != null) {
      backgroundClip.start();
    }
    started = true
    update
  }

  def stop() {
    started = false
    clips.foreach(clip => clip.stop)
    if (backgroundClip != null) {
      backgroundClip.stop();
    }
  }

  def loadClips(): List[AudioClip] = {
    return List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).map(i => new AudioClip(openAL, new File(soundBank, i + ".wav")))
  }

  def muteClips() {
    for (clip <- clips) {
      clip.volume_=(0.001f)
    }
    if (backgroundClip != null) {
      backgroundClip.volume_=(0.001f)
    }
  }

  def square(x: Float) = x * x

  def setGain(clip: AudioClip, x: Float, y: Float) {
    val index = clips.indexOf(clip)
    var point = index match {
      case 0 => new Point(0, 0)
      case 1 => new Point(1 / 3f, 0)
      case 2 => new Point(2 / 3f, 0)
      case 3 => new Point(1, 0)
      case 4 => new Point(0, 0.5f)
      case 5 => new Point(1 / 3f, 0.5f)
      case 6 => new Point(2 / 3f, 0.5f)
      case 7 => new Point(1, 0.5f)
      case 8 => new Point(0, 1)
      case 9 => new Point(1 / 3f, 1)
      case 10 => new Point(2 / 3f, 1)
      case 11 => new Point(1, 1)
    }
    val distance = Math.sqrt(square(x - point.x) + square(y - point.y))
    clip.volume_=(Math.max(1 - distance * 3, 0.001f).asInstanceOf[Float])
  }

  def update {
    if (started) {
      if (mute) {
        muteClips()
        return
      }
      for (clip <- clips) {
        setGain(clip, x, y)
      }
      val position = new Tuple3F((x - .5f) * 3, 0, (y - .5f) * 3);
      for (i <- 0 until 4)
        clips(i).position_=(position)
      if (backgroundClip != null) {
        backgroundClip.volume_=(1)
        backgroundClip.position_=(position);
      }
    }
  }

  override def moveTo(x: Float, y: Float) {
    super.moveTo(x, y)
    update
  }

  override def setMute(mute: Boolean) {
    super.setMute(mute)
    update
  }
}