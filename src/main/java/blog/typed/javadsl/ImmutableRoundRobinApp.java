package blog.typed.javadsl;

import java.io.IOException;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Actor;

public class ImmutableRoundRobinApp {

  public static void main(String[] args) throws IOException {
    Behavior<Void> root = Actor.deferred(ctx -> {
      ActorRef<Worker.Command> workerPool =
        ctx.spawn(ImmutableRoundRobin.roundRobinBehavior(3, Worker.behavior()), "workerPool");
      for (int n = 1; n <= 20; n++) {
        workerPool.tell(new Worker.Job(String.valueOf(n)));
      }

      return Actor.empty();
    });
    ActorSystem<Void> system = ActorSystem.create(root, "RoundRobin");
    try {
      System.out.println("Press ENTER to exit the system");
      System.in.read();
    } finally {
      system.terminate();
    }
  }
}

