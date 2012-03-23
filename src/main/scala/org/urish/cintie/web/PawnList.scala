package org.urish.cintie.web

class PawnList (var pawns: java.util.Collection[PawnInfo], name: String = "") {
  def getPawns() = pawns
  def getName() = name
}