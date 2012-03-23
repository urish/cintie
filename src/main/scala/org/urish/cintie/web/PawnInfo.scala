package org.urish.cintie.web
import scala.reflect.BeanProperty

class PawnInfo(@BeanProperty val id: Int, @BeanProperty val x: Float, @BeanProperty val y: Float) {
}