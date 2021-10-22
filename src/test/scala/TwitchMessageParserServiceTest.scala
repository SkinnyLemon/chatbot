import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service._
import model._

class TwitchMessageParserServiceTest extends AnyWordSpec with Matchers {
    
    val tagsMap = Map(("room-id", "1337"), ("display-name", "Ronni"), ("emotes", "25:0-4,12-16/1902:6-10"), ("tmi-sent-ts", "1507246572675"), ("user-id", "1234"), ("color", "#0D4200"), 
    ("badges", "#global_mod/1,turbo/1"), ("id", "b34ccfc7-4977-403a-8a94-33c6bac34fb8"), ("badge-info", ""), ("mod", "0"), ("subscriber", "0"), ("turbo", "1"), ("user-type", "global_mod"))

    "A channel parser" should {
        val parser = new ChannelParser()
        val channelName = "MyTwitchChannelName"
        val channel = parser.parseToChannel(tagsMap, channelName)

        "return a channel object" in { 
            channel shouldBe a [Channel]
        }
        "should create an object with the desired name" in {
            channel.name should be (channelName) // @ Tobi, gibt es hier eine best practise wegen String?
        }
        "should hold the roomId as it passed in the tags" in {
            channel.roomId should be (tagsMap("room-id")) 
        }
    }

    "A user parser" should {
        val parser = new UserParser()
        val userName = "RonnieRonaldo"
        val user = parser.parseToUser(tagsMap, userName)

        "return a user object" in { 
            user shouldBe a [User]
        }
        "should create an user with the desired username" in {
            user.userName should be (userName)
        }
        "should create an user with the desired display name" in {
            user.displayName should be (tagsMap("display-name"))
        }
        "should hold the userId as it is passed in the tags" in {
            user.userId should be (tagsMap("user-id")) 
        }
    } 

    "A Message parser" should {
        val parser = new MessageParser()
        val msg = "kappa"
        val message = parser.parseToMessage(tagsMap, msg)

        "return a message object" in { 
            message shouldBe a [Message]
        }
        "should hold a message" in {
            message.message should be (msg)
        }
        "should have a timestamp of type Long" in {
            message.timeStamp shouldBe a [Long]
        }
        "should hold a sequence" in {
            message.emotes shouldBe a [Seq[_]]
        }
        "should hold a sequence of emotes" in {
            message.emotes(0) shouldBe a [Emote]
        }
    } 
}
