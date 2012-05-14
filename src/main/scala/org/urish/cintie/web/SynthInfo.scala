package org.urish.cintie.web
import scala.reflect.BeanProperty

class SynthInfo() {
  @BeanProperty var preset: Int = -1
  @BeanProperty var octave: Int = -1
  @BeanProperty var volume: Int = -1
}