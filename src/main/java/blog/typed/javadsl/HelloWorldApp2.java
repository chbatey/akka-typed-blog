package blog.typed.javadsl;

import java.io.IOException;

import akka.actor.typed.Behavior;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Actor;

public class HelloWorldApp2 {
  public static void main(String[] args) throws IOException {
    Behavior<Void> root = Actor.deferred(ctx -> {
      ActorRef<Greeter2.Command> greeter =
        ctx.spawn(Greeter2.greeterBehavior(), "greeter");
      greeter.tell(new Greeter2.WhoToGreet("World"));
      greeter.tell(new Greeter2.Greet());

      return Actor.empty();
    });
    ActorSystem<Void> system = ActorSystem.create(root, "HelloWorld");
    try {
      System.out.println("Press ENTER to exit the system");
      System.in.read();
    } finally {
      system.terminate();
    }
  }
}

