package org.urish.cintie.util
import java.io.File
import java.io.InputStream
import org.apache.commons.io.IOUtils
import java.io.FileOutputStream
import java.lang.reflect.Field
import java.io.IOException

object LibraryLoader {
  def loadEmbededLibrary(dllName: String) {
    val tempDir = createTempDirectory();

    val libStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dllName)
    val tempFile = new File(tempDir, dllName)
    val outputStream = new FileOutputStream(tempFile)
    IOUtils.copy(libStream, outputStream)
    outputStream.close
    libStream.close
    tempFile.deleteOnExit()

    System.setProperty("java.library.path", tempDir.getPath())
    System.setProperty("jna.library.path", tempDir.getPath())
    val fieldSysPath = classOf[ClassLoader].getDeclaredField("sys_paths");
    fieldSysPath.setAccessible(true);
    fieldSysPath.set(null, null);
    tempDir.deleteOnExit()
  }

  private def createTempDirectory(): File = {
    val temp = File.createTempFile("temp", String.valueOf(System.nanoTime()));

    if (!(temp.delete())) {
      throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
    }

    if (!(temp.mkdir())) {
      throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
    }

    return temp;
  }
}