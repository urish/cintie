package org.urish.cintie.web

import scala.collection.JavaConversions.seqAsJavaList
import org.urish.cintie.engine.CintieEngine
import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.Consumes
import javax.ws.rs.core.MediaType

object CintieController {
  val engine = new CintieEngine();
}

@Path("/")
class CintieController {
  val engine = CintieController.engine

  @GET
  @Path("/start")
  def start() = engine.start

  @GET
  @Path("/stop")
  def stop() = engine.stop

  @GET
  @Path("/pawns")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def getPawn() = new PawnList(seqAsJavaList[PawnInfo](engine.players.zipWithIndex.map { case (player, index) => new PawnInfo(index + 1, player.x, player.y) }))

  @POST
  @Path("/pawns/{id}")
  def updatePawn(@PathParam("id") id: Int, @FormParam("x") x: Float, @FormParam("y") y: Float, @FormParam("on") on: String) {
    val player = engine.player(id - 1)
    player.moveTo(x, y)
    if ((on != null) && !(on.equals(""))) {
      player.setMute(!on.equalsIgnoreCase("true"));
    }
  }

  @POST
  @Path("/synth")
  @Consumes(Array(MediaType.APPLICATION_JSON))
  def updatePreset(synthInfo: SynthInfo) {
    if (synthInfo.preset >= 0) {
      engine.synthPlayer.preset = synthInfo.preset
    }
    if (synthInfo.octave >= 0) {
      engine.synthPlayer.octave = synthInfo.octave
    }
    if (synthInfo.volume >= 0) {
      engine.synthPlayer.volume = synthInfo.volume.shortValue()
    }
    if (synthInfo.pitch > 0.001f) {
      engine.pitch = synthInfo.pitch
    }
  }
}