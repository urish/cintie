package org.urish.cintie.engine
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl
import java.lang.Math
import com.synthbot.audioplugin.vst.vst2.JVstHost2
import javax.sound.midi.ShortMessage
import com.synthbot.audioio.vst.JVstAudioThread
import org.urish.cintie.util.LibraryLoader
import org.urish.cintie.util.VstPresetLoader
import org.urish.openal.OpenAL
import org.urish.openal.Tuple3F

abstract class Player {
  protected var _x: Float = 0;
  protected var _y: Float = 0;
  protected var _mute: Boolean = false;
  def x = _x
  def y = _y
  def mute = _mute

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
    clips(i - 1).volume_=(value)
  }

  def muteClips() {
    setGain(1, 0.001f)
    setGain(2, 0.001f)
    setGain(3, 0.001f)
    setGain(4, 0.001f)
    if (backgroundClip != null) {
      backgroundClip.volume_=(0.001f)
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
