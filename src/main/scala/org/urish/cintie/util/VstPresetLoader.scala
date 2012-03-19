package org.urish.cintie.util
import java.io.File
import com.synthbot.audioplugin.vst.vst2.JVstHost2
import org.apache.commons.io.IOUtils
import org.apache.commons.io.FileUtils
import java.io.RandomAccessFile
import com.synthbot.audioplugin.vst.vst2.JVstPersistence

object VstPresetLoader {
	def loadVstPreset(vst: JVstHost2, file: File) {
	  val tempFile = File.createTempFile("preset", "fxp");
	  try {
	    FileUtils.copyFile(file, tempFile);
	    var randomAccessFile = new RandomAccessFile(tempFile, "rw")
	    randomAccessFile.seek(20)
	    randomAccessFile.writeInt(0);
	    randomAccessFile.close();
	    JVstPersistence.loadPreset(vst, tempFile)
	  } finally {
	    tempFile.delete();
	  }
	}
}