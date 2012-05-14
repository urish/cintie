package org.urish.cintie.engine
import java.io.File
import org.urish.openal.OpenAL
import org.urish.openal.Tuple3F
import java.util.Properties
import java.io.FileInputStream
import java.io.IOException

abstract class Player {
  protected var _x: Float = 0;
  protected var _y: Float = 0;
  protected var _mute: Boolean = false;
  protected var _gain: Float = 1.0f;
  def x = _x
  def y = _y
  def mute = _mute
  def gain = _gain

  def start()
  def stop()
  def moveTo(x: Float, y: Float) = { this._x = x; this._y = y; }
  def setMute(mute: Boolean) = { this._mute = mute }
}

class FourSourcePlayer(openAL: OpenAL, soundBank: File) extends Player {
  private var started = false;
  val clips = List(1, 2, 3, 4).map(i => new AudioClip(openAL, new File(soundBank, i + ".wav")))
  val backgroundClipFile = new File(soundBank, "background.wav")
  val backgroundClip = if (backgroundClipFile.exists()) new AudioClip(openAL, backgroundClipFile) else null;
  val propertiesFile = new File(soundBank, "player.ini")

  try {
    val properties = new Properties()
    properties.load(new FileInputStream(propertiesFile))
    _gain = properties.getProperty("gain", "1.0").toFloat
  } catch {
    case e: IOException => /* pass */
  }

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

  def setGain(i: Int, value: Float) {
    if (value <= 0) {
      setGain(i, 0.0001f)
      return
    } else if (value > 1) {
      setGain(i, 1)
      return
    }
    clips(i - 1).volume_=(gain * value)
  }

  def muteClips() {
    setGain(1, 0.001f)
    setGain(2, 0.001f)
    setGain(3, 0.001f)
    setGain(4, 0.001f)
    if (backgroundClip != null) {
      backgroundClip.volume_=(gain * 0.001f)
    }
  }

  def update {
    if (started) {
      if (mute) {
        muteClips()
        return
      }
      setGain(1, x * y)
      setGain(2, x * (1 - y))
      setGain(3, (1 - x) * y)
      setGain(4, (1 - x) * (1 - y))
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
