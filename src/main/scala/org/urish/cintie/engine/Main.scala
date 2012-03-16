package org.urish.cintie.engine

object Main {
  def main(args: Array[String]) {
    val engine = new CintieEngine()
    engine.start;
    Thread.sleep(1000000);
  }
}