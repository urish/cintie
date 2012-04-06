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
import org.urish.openal.OpenAL
import org.urish.openal.jna.AL
import org.apache.commons.io.IOUtils
import org.apache.commons.io.FileUtils
import org.urish.openal.SourceState
import org.urish.openal.Tuple3F

class AudioClip(val openAL: OpenAL, val file: File) {
  var in = AudioSystem.getAudioInputStream(file);
  val format = in.getFormat();
  val frameSize = format.getFrameSize()
  val encoding = format.getEncoding();

  if (!(encoding.equals(AudioFormat.Encoding.PCM_SIGNED) || encoding
    .equals(AudioFormat.Encoding.PCM_UNSIGNED))) {
    throw new UnsupportedAudioFileException("not PCM audio !");
  }

  val source = openAL.createSource();
  var buffer = openAL.createBuffer();
  val pcmData = IOUtils.toByteArray(in);
  val monoPcmData = if (format.getChannels() > 1) toMono(pcmData) else pcmData
  buffer.addBufferData(AL.AL_FORMAT_MONO16, monoPcmData, monoPcmData.length, format.getSampleRate().intValue())
  source.setBuffer(buffer);
  source.setLooping(true);

  def start() {
    source.play();
  }

  def stop() {
    source.stop();
  }
  
  def playing() = source.getSourceState() == SourceState.PLAYING;

  def volume_=(volume: Float) {
    source.setGain(volume);
  }
  
  def position_=(position: Tuple3F): Unit = {
    System.out.println(position)
    source.setPosition(position)
  }
  
  private def toMono(pcmData: Array[Byte]): Array[Byte] = {
    val result: Array[Byte] = new Array[Byte](pcmData.length/2);
    for (i <- 0 until result.length / 2) {
      result(i*2) = pcmData(i*4);
      result(i*2+1) = pcmData(i*4+1);
    }
    return result
  }
}