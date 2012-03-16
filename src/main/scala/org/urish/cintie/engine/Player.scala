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
  val clips = List(1, 2, 3, 4).map(i => {
    val clip = AudioSystem.getClip();
    clip.open(AudioSystem.getAudioInputStream(new File(soundBank, i + ".wav")));
    System.out.println(clip.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl].getValue());
    clip.start();
    clip.loop(9999)
    clip
  });
  update

  def setGain(i: Int, value: Float) {
    var gainControl = clips(i - 1).getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl];
    gainControl.setValue(Math.log10(value).floatValue() * 20);
  }

  def update {
    setGain(1, x * y)
    setGain(2, x * (1 - y))
    setGain(3, (1 - x) * y)
    setGain(4, (1 - x) * (1 - y))
  }
}