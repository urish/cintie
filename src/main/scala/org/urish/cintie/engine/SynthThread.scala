package org.urish.cintie.engine
import org.urish.jfluid.FluidEvent
import org.urish.jfluid.FluidsynthApi
import org.urish.jfluid.Synth
import org.urish.openal.Buffer
import org.urish.openal.OpenAL
import org.urish.openal.Source
import org.urish.openal.SourceState

import javax.sound.sampled.AudioFormat

class SynthThread(val openAL: OpenAL, val source: Source, val fluidsynth: FluidsynthApi, val synth: Synth) extends Runnable {
  val SAMPLES_PER_BUFFER = 2048
  val audioFormat = new AudioFormat(44100, 16, 1, true, false);
  var terminating = false

  val thread = new Thread(this, "SynthThread")
  val sequencer = fluidsynth.new_fluid_sequencer()
  var sequencerId: Short = fluidsynth.fluid_sequencer_register_fluidsynth(sequencer, synth)

  def start() {
    thread.setDaemon(true)
    thread.setPriority(Thread.MAX_PRIORITY)
    thread.start()
  }

  def stop() {
    thread.interrupt()
    terminating = true
  }

  def createEvent(): FluidEvent = {
    val event = fluidsynth.new_fluid_event()
    fluidsynth.fluid_event_set_dest(event, sequencerId)
    return event
  }

  def sendNote(channel: Int, key: Short, velocity: Short, duration: Int) {
    val event = createEvent()
    fluidsynth.fluid_event_note(event, channel, key, velocity, duration)
    fluidsynth.fluid_sequencer_send_now(sequencer, event)
  }
  
  def currentTick(): Int = {
    fluidsynth.fluid_sequencer_get_tick(sequencer)
  }

  def run() {
    val buffers = Array[Buffer](openAL.createBuffer(), openAL.createBuffer(), openAL.createBuffer())
    var nextBuffer = 0
    val buffer = new Array[Byte](SAMPLES_PER_BUFFER * 2)
    
    try {
      fluidsynth.fluid_synth_start(synth);
      while (!terminating) {
        fluidsynth.fluid_synth_write_s16(synth, SAMPLES_PER_BUFFER, buffer, 0, 1, buffer, 0, 1)
        if (source.getQueuedBufferCount() >= buffers.length) {
          if (source.getSourceState() != SourceState.PLAYING) {
            source.play();
          }
          while (source.getProcessedBufferCount() == 0) {
            Thread.sleep(1)
          }
          source.unqueueBuffer(buffers(nextBuffer))
        }
        buffers(nextBuffer).addBufferData(audioFormat, buffer)
        source.queueBuffer(buffers(nextBuffer))
        if (source.getSourceState() == SourceState.INITIAL) {
          source.play();
        }
        nextBuffer = (nextBuffer + 1) % buffers.length
      }
    } finally {
      fluidsynth.delete_fluid_sequencer(sequencer);
      for (buffer <- buffers) {
        buffer.close();
      }
    }
  }
}