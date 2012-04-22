package org.urish.cintie.web

import scala.collection.JavaConversions.seqAsJavaList

import org.springframework.stereotype.Component
import org.urish.cintie.engine.CintieEngine

import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

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
  @Produces(Array("application/json"))
  def getPawn() = new PawnList(seqAsJavaList[PawnInfo](engine.players.zipWithIndex.map { case (player, index) => new PawnInfo(index + 1, player.x, player.y) }))

  @POST
  @Path("/pawns/{id}")
  def updatePawn(@PathParam("id") id: Int, @FormParam("x") x: Float, @FormParam("y") y: Float, @FormParam("on") on: String) {
    val player = engine.player(id - 1)
    player.moveTo(x, y)
    if ((on != null) && !(on.equals(""))) {
      player.setMute(!on.equalsIgnoreCase("true"));
    }
    throw new Exception("Sux")
  }
}