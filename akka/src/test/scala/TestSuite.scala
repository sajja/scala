import akka.actor.ActorSystem
import org.scalatest._
import Matchers._
import akka.testkit.TestProbe
import com.example.akka.actor.example.Device
//import com.example.akka.actor.example.Device.ReadTemperature

class TestSuite extends FlatSpec {
  "reply with empty reading if no temperature is known" should "xxx" in {
    import com.example.akka.actor.example.Device._
    implicit val system = ActorSystem("input")
    val probe = TestProbe()
    val deviceActor = system.actorOf(Device.props("group", "device"))

//    deviceActor ! Device.ReadTemperature(101)
    deviceActor.tell(ReadTemperature(42), probe.ref)
    val response = probe.expectMsgType[RespondTemperature]
    response.requestId should ===(42)
    response.value should ===(None)
  }
}
