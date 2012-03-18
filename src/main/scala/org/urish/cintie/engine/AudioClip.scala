package org.urish.cintie.engine
import java.io.File
import java.lang.Math

import scala.util.control.Breaks.break
import scala.util.control.Breaks.breakable

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.FloatControl
import javax.sound.sampled.SourceDataLine
import javax.sound.sampled.UnsupportedAudioFileException

class AudioClip(val file: File) extends Runnable {
  var in = AudioSystem.getAudioInputStream(file);
  val format = in.getFormat();
  val frameSize = format.getFrameSize()
  val encoding = format.getEncoding();
  var buffer = new Array[Byte](32 * 1024); // 32k is arbitrary

  if (!(encoding.equals(AudioFormat.Encoding.PCM_SIGNED) || encoding
    .equals(AudioFormat.Encoding.PCM_UNSIGNED))) {
    throw new UnsupportedAudioFileException("not PCM audio !");
  }

  val info = new DataLine.Info(classOf[SourceDataLine], format);
  val line: SourceDataLine = AudioSystem.getLine(info).asInstanceOf[SourceDataLine];
  var playThread: Thread = null;
  var playing: Boolean = false;

  def start() {
    line.open();
    playing = true;
    playThread = new Thread(this);
    playThread.setDaemon(true);
    playThread.start();
    line.start();
  }

  def stop() {
    playing = false
    line.stop()
  }

  def setVolume(volume: Float) {
    var gainControl = line.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl];
    gainControl.setValue(Math.log10(volume).floatValue() * 20);
  }

  def run() {
    var readPoint: Int = 0;
    var bytesRead: Int = 0;

    while (playing) {
      bytesRead = in.read(buffer, readPoint, buffer.length - readPoint);
      if (bytesRead == -1) {
        in.close()
        in = AudioSystem.getAudioInputStream(file);
        bytesRead = in.read(buffer, readPoint, buffer.length - readPoint);
      }
      val frames = bytesRead / frameSize;
      val leftover = bytesRead % frameSize;
      line.write(buffer, readPoint, bytesRead - leftover);
      System.arraycopy(buffer, bytesRead, buffer, 0, leftover);
      readPoint = leftover;
    }
  }
}