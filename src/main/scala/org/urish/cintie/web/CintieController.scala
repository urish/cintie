package org.urish.cintie.web

import org.springframework.stereotype.Component
import org.urish.cintie.engine.CintieEngine
import org.urish.cintie.engine.FourSourcePlayer

import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@Path("/")
@Component
class CintieController {
  val engine = new CintieEngine();

  @GET
  @Path("/start")
  def start() = engine.start

  @GET
  @Path("/stop")
  def stop() = engine.stop

  @GET
  @Path("/pawns")
  def getPawn() = "{\"x\": " + 0 + ", \"y\": " + 0 + "}"

  @POST
  @Path("/pawns/{id}")
  def updatePawn(@PathParam("id") id: Int, @FormParam("x") x: Float, @FormParam("y") y: Float) {
    val player = engine.player(id - 1)
    player.x = x;
    player.y = y;
    if (player.isInstanceOf[FourSourcePlayer]) {
      player.asInstanceOf[FourSourcePlayer].update;
    }
    throw new Exception("Sux")
  }
}