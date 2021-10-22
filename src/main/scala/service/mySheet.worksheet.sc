
val progLang = List("C++", "JavaScript" , "Scala" , "Python")
val mapaaa = progLang.zipWithIndex.map{ case (v,i) => (i,v) }.toMap


val message = "@badge-info=;badges=;color=;display-name=chrisb2fhkfc;emotes=;first-msg=1;flags=;id=7e542398-9dd7-43d8-b440-29695a1b9114;mod=0;room-id=156037856;subscriber=0;tmi-sent-ts=1634907933948;turbo=0;user-id=730960175;user-type= :chrisb2fhkfc!chrisb2fhkfc@chrisb2fhkfc.tmi.twitch.tv PRIVMSG #fextralife :can you rife pets"
val (rawTags, rawMessage) = message.span(_ != ':')

val testRaw = rawTags.split(";")


var tagMap = rawTags.split(";").map(_.split('=')).collect{ 
    case Array(k: String, v: String) => (k,v) 
    case Array(k: String) => (k, "")
    }.toMap




def toSaveTouple(arr: Array[String]): (String, String) = 
    val k = arr(0)
    val v = if(arr.isDefinedAt(1)) arr(1) else ""
    (k, v)

var anotherTagMap = rawTags.split(";").map(_.split('=')).collect{ 
    case a => (a, if(a.isDefinedAt(1)) a(1) else "")
    }.toMap



var anotherTagMa2p = rawTags.split(";").map(_.split('=')).collect{ 
    case a => toSaveTouple(a)
    }.toMap



//tagMap.foreach(println)


tagMap("user-type")
tagMap("mod")
tagMap("user-id")
tagMap("color")

//anotherTagMap("color")
//anotherTagMap("mod")


var tagMap2: Array[Array[String]] = rawTags.split(";").map(_.split("="))

val tag3 = tagMap2.foreach(e =>  print(e(0)))

//var mapmap: Map[String, String] = ("a" -> "s")// = tagMap2.foreach(e => mapmap.)

//val tag4 = tagMap2.foreach(e => {mapmap += toSaveTouple(e)})

