package service
import de.htwg.rs.chatbot.TwitchConsumer

class TwitchConsumerImpl extends TwitchConsumer {

  override def onMessage(message: String): Unit =

    val (rawTags, rawMessage) = message.span(_ != ' ')

    val tagMap = rawTags
      .split(";")
      .map(_.split('='))
      .collect {
        case Array(k: String, v: String) => (k, v)
        case Array(k: String)            => (k, "")
      }
      .toMap

}

//@badge-info=;
//badges=;color=;
//display-name=chrisb2fhkfc;
//emotes=;
//first-msg=1;
//flags=;
//id=7e542398-9dd7-43d8-b440-29695a1b9114;mod=0;room-id=156037856;subscriber=0;tmi-sent-ts=1634907933948;turbo=0;user-id=730960175;user-type= :chrisb2fhkfc!chrisb2fhkfc@chrisb2fhkfc.tmi.twitch.tv PRIVMSG #fextralife :can you rife pets
