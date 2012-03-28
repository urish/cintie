package org.urish.cintie.web

import scala.collection.JavaConversions._
import org.springframework.stereotype.Component
import org.urish.cintie.engine.CintieEngine
import org.urish.cintie.engine.FourSourcePlayer
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.PathParam
import javax.ws.rs.FormParam
import javax.ws.rs.POST

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
  def getPawn() = new PawnList(seqAsJavaList[PawnInfo](engine.players.zipWithIndex.map{ case (player, index) => new PawnInfo(index+1, player.x, player.y) }))

  @POST
  @Path("/pawns/{id}")
  def updatePawn(@PathParam("id") id: Int, @FormParam("x") x: Float, @FormParam("y") y: Float) {
    val player = engine.player(id - 1)
    player.moveTo(x, y)
    throw new Exception("Sux")
  }
}