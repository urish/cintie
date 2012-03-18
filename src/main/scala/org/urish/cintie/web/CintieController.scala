package org.urish.cintie.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.urish.cintie.engine.CintieEngine
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestParam
import org.urish.cintie.engine.FourSourcePlayer

@Controller
class CintieController {
  val engine = new CintieEngine();

  @RequestMapping(value = Array("/start"), method = Array(RequestMethod.GET))
  @ResponseBody
  def start() = engine.start

  @RequestMapping(value = Array("/stop"), method = Array(RequestMethod.GET))
  @ResponseBody
  def stop() = engine.stop

  @RequestMapping(value = Array("/pawns/{id}"), method = Array(RequestMethod.GET))
  @ResponseBody
  def getPawn(@PathVariable("id") id: Int) = "{\"x\": " + 0 + ", \"y\": " + 0 + "}"

  @RequestMapping(value = Array("/pawns/{id}"), method = Array(RequestMethod.POST))
  @ResponseBody
  def updatePawn(@PathVariable("id") id: Int, @RequestParam("x") x: Float, @RequestParam("y") y: Float) {
    val player = engine.player(id-1)
    player.x = x;
    player.y = y;
    player.asInstanceOf[FourSourcePlayer].update;
  }
}