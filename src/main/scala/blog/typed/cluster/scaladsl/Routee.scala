package blog.typed.cluster.scaladsl

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Actor

object Routee {

  val PingServiceKey = Receptionist.ServiceKey[Ping]("pingService")

  final case class Ping(replyTo: ActorRef[Pong.type])
  final case object Pong

  val behavior: Behavior[Ping] =
    Actor.deferred { ctx ⇒
      ctx.system.receptionist ! Receptionist.Register(PingServiceKey, ctx.self, ctx.system.deadLetters)

      Actor.immutable[Ping] { (ctx, msg) ⇒
        msg match {
          case Ping(replyTo) ⇒
            println(s"Routee ${ctx.self} got ping")
            Actor.same
        }
      }
    }

}
