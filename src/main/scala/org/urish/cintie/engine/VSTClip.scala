package org.urish.cintie.engine
import javax.sound.sampled.AudioFormat
import java.io.OutputStream
import com.synthbot.audioplugin.vst.vst2.JVstHost2
import org.urish.openal.Source
import alpatch.SourceOutputStream
import org.urish.cintie.util.SourceOutputStream

class VSTClip(val vst: JVstHost2, val source: Source) extends Runnable {

  val ShortMaxValueAsFloat: Float = Short.MaxValue.toFloat;

  val numOutputs = vst.numOutputs();
  val numAudioOutputs = Math.min(2, numOutputs);
  val blockSize = vst.getBlockSize();
  val fInputs = Array.ofDim[Float](vst.numInputs(), blockSize);
  val fOutputs = Array.ofDim[Float](numOutputs, blockSize);
  val bOutput = new Array[Byte](numAudioOutputs * blockSize * 2);

  val audioFormat = new AudioFormat(vst.getSampleRate().intValue, 16, numAudioOutputs, true, false);
  val outputStream = new SourceOutputStream(CintieEngine.alfactory.al, source, audioFormat)

  /**
   * Converts a float audio array [-1,1] to an interleaved array of 16-bit samples
   * in little-endian (low-byte, high-byte) format.
   */
  def floatsToBytes(fData: Array[Array[Float]], bData: Array[Byte]): Array[Byte] = {
    var index = 0;
    for (i <- 0 until blockSize) {
      for (j <- 0 until numAudioOutputs) {
        val sval = (fData(j)(i) * ShortMaxValueAsFloat).shortValue();
        bData(index) = (sval & 0x00FF).byteValue();
        bData(index + 1) = ((sval & 0xFF00) >> 8).byteValue();
        index += 2
      }
    }
    return bData;
  }

  def run() {
    while (true) {
      vst.processReplacing(fInputs, fOutputs, blockSize);
      outputStream.write(floatsToBytes(fOutputs, bOutput), 0, bOutput.length);
    }
  }
}