package com.nsfl.tpetracker.model.player

import com.nsfl.tpetracker.Logger
import com.nsfl.tpetracker.model.position.Position
import com.nsfl.tpetracker.model.team.Team
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class MyBBPlayerParser {

    private val inputDateFormat = SimpleDateFormat("MM-dd-yyyy, HH:mm a")
    private val lastSeenDateFormat = SimpleDateFormat("yyyy/MM/dd")

    fun parseActivePlayers() = ArrayList<PlayerParser.ParsedPlayer>().apply {
        Team.values().forEach { addAll(parsePlayers(it)) }
    }

    fun parsePlayers(team: Team): List<PlayerParser.ParsedPlayer> {

        val playerList = ArrayList<PlayerParser.ParsedPlayer>()
        val documentList = ArrayList<Document>()

        val firstDocument = connect("https://forums.sim-football.com/forumdisplay.php?fid=${team.id}") ?: throw Exception()
        documentList.add(firstDocument)

        val pageCount = parsePageCount(firstDocument.body().getElementsByClass("pages"))
        if(pageCount>1){
            for (i in 2..pageCount) {
                println("Adding page $i")
                documentList.add(connect("https://forums.sim-football.com/forumdisplay.php?fid=${team.id}&page=${i}") ?: throw Exception())
            }
        }

        documentList.forEach { document ->
            document.body().getElementsByClass("inline_row")?.forEach { row -> row.allElements
                    ?.filter { element ->
                        element.toString().let {
                            it.startsWith("<td class=\"trow")
                                    && !it.contains("Moved:")
                                    && it.contains(">(S")
                        }
                    }?.forEach { element ->
                        element.toString().let {
                            //println(parsePlayerInfo(it))

                            val playerInfo = parsePlayerInfo(it).split("?")

                            if (playerInfo.size == 3) {

                                try{
                                    val playerId = parsePlayerID(it)
                                    //println(playerId)
                                    val playerPostDoc = connect("https://forums.sim-football.com/showthread.php?tid=${playerId}") ?: throw Exception()

                                    try {
                                        val playerPost = playerPostDoc.body()
                                                            .getElementsByClass("post")[0]

                                        val author = playerPost.getElementsByClass("post_author")[0]
                                        val user = author.getElementsByClass("largetext").text()

                                        val playerLink = author.getElementsByAttribute("href")[0].attr("href")
                                        val playerProfileDoc = connect(playerLink) ?: throw Exception()
                                        val playerProfile = playerProfileDoc.body().getElementsByClass("trow2")

                                        val attributes = playerPost.getElementsByClass("post_body").toString()

                                        playerList.add(
                                                PlayerParser.ParsedPlayer(
                                                        playerId,
                                                        user.replace("'", "’"),
                                                        playerInfo[1].trim(),
                                                        team,
                                                        Position.fromAbbreviation(playerInfo[2].trim()),
                                                        playerInfo[0].trim().let { info ->
                                                            if (info.length == 2) {
                                                                "S0${info.substring(1)}"
                                                            } else {
                                                                info
                                                            }
                                                        },
                                                        parsePlayerTPE(it),
                                                        parsePlayerAttribute(attributes, "strength:"),
                                                        parsePlayerAttribute(attributes, "agility:"),
                                                        parsePlayerAttribute(attributes, "arm:"),
                                                        parsePlayerAttribute(attributes, "intelligence:"),
                                                        parsePlayerAttribute(attributes, "throwing accuracy:"),
                                                        parsePlayerAttribute(attributes, "tackling:"),
                                                        parsePlayerAttribute(attributes, "speed:"),
                                                        parsePlayerAttribute(attributes, "hands:"),
                                                        parsePlayerAttribute(attributes, "pass blocking:"),
                                                        parsePlayerAttribute(attributes, "run blocking:"),
                                                        parsePlayerAttribute(attributes, "endurance:"),
                                                        parsePlayerAttribute(attributes, "kick power:"),
                                                        parsePlayerAttribute(attributes, "kick accuracy:"),
                                                        parseLastSeen(playerProfile, playerId)
                                                )
                                        )
                                } catch (exception: Exception) {
                                    val sw = StringWriter()
                                    exception.printStackTrace(PrintWriter(sw))
                                    val exceptionAsString = sw.toString()

                                    Logger.error("Issue parsing player with Player ID: $playerId ! $exceptionAsString")
                                }

                        } catch (exception: Exception){
                                    Logger.error("Issue parsing thread table row! $exception")
                                }
                            }
                    }
                  }
            }
        }

        return playerList
    }

    private fun parsePageCount(pages: Elements): Int {
        return try {
            val pagesString = pages.text()
            val regex = """\(([0-9]+)\)""".toRegex()
            val matchResult = regex.find(pagesString) ?: throw Exception()
            return matchResult.groupValues[1].toInt()
        } catch (exception: Exception) {
            1
        }
    }

    private fun parsePlayerInfo(elementString: String): String {
        val startIndex = elementString.indexOf(">(S") + 1
        val endIndex = elementString.indexOf("<", startIndex)
        return elementString.substring(startIndex, endIndex)
                .replace("–", "-")
                .replace("(", "")
                .replace(")", "")
                .replace("'", "’")
                .replaceFirst("-", "?")
                .reversed()
                .replaceFirst("-", "?")
                .reversed()
    }

    private fun parsePlayerTPE(elementString: String): Int {
        return try {
            val startIndex = elementString.indexOf("TPE:")
            val endIndex = elementString.indexOf("<", startIndex)

            elementString.substring(startIndex, endIndex)
                    .replace(Pattern.compile("[^0-9.]").toRegex(), "")
                    .toInt()
        } catch (exception: Exception) {
            50
        }
    }

    private fun parsePlayerID(elementString: String): String {

            val regex = """href="showthread.php\?tid=([0-9]+)">\(""".toRegex()
            val matchResult = regex.find(elementString) ?: throw Exception()
            return matchResult.groupValues[1]

    }

    fun parseUserName(playerID: String): String{
      try {
        val con_user = connect("https://forums.sim-football.com/showthread.php?tid=$playerID") ?: throw Exception()
        val user = con_user
                .body()
                .getElementsByClass("post")[0]
                .getElementsByClass("post_author")[0]
                .getElementsByClass("largetext")
                .text()

        return user.replace("'", "’")
      } catch (exception: Exception) {
        return playerID
      }
    }

    private fun parseProfileId(elementString: String): String {
        val startIndex = elementString.indexOf("showuser=") + 9
        val endIndex = elementString.indexOf("\"", startIndex)
        return elementString.substring(startIndex, endIndex)
    }

    private fun parsePlayerAttribute(post: String, attributeName: String): Int {
        return try {

            var attribute = ""
            var started = false
            var finished = false
            var index = post.indexOf(attributeName, ignoreCase = true)

            while (!finished) {

                val char = post[index]

                if (char.isDigit()) {
                    started = true
                    attribute += char
                } else if (started) {
                    finished = true
                }

                index++
            }

            attribute.toInt()
        } catch (exception: Exception) {
            -999
        }
    }

    private fun parseLastSeen(profile_elements: Elements, playerId: String): String {

        val visit_elements = profile_elements.filter { element ->
            element.toString().contains("Last Visit:")
        }

        val lastSeenString = visit_elements[0].nextElementSibling().toString()

        //val regex = """([0-9]{2}-[0-9]{2}-[0-9]{4}, [0-9]{2}:[0-9]{2} [PA]M)""".toRegex()
        val regex = """([0-9]{2}-[0-9]{2}-[0-9]{4}).*?([0-9]{2}:[0-9]{2} [PA]M)""".toRegex()
        return try {
            val matchResult = regex.find(lastSeenString) ?: throw Exception()
            lastSeenDateFormat.format(inputDateFormat.parse(matchResult.groupValues[1] + ", " + matchResult.groupValues[2]))
        } catch (e: Exception) {
            Logger.info("Unable to parse last seen string: $lastSeenString for player ID: $playerId")
            //Set a default date for those we cant parse due to privacy settings or other issues.
            lastSeenDateFormat.format(inputDateFormat.parse("01-01-2000, 00:01 AM"))
        }

    }

    private fun connect(url: String): Document? {
        var i = 0
        while (i<3) {
            try {
                return Jsoup
                        .connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0")
                        .get()
            } catch (exception: Exception) {
                Logger.error("Jsoup.connect failed. for URL: $url \n $exception")
                Thread.sleep(5000)
                i++
            }
        }
        return null
    }

    data class ParsedPlayer(
            val id: String,
            val user: String,
            val name: String,
            val team: Team,
            val position: Position,
            val draftYear: String,
            val tpe: Int,
            val strength: Int,
            val agility: Int,
            val arm: Int,
            val intelligence: Int,
            val throwingAccuracy: Int,
            val tackling: Int,
            val speed: Int,
            val hands: Int,
            val passBlocking: Int,
            val runBlocking: Int,
            val endurance: Int,
            val kickPower: Int,
            val kickAccuracy: Int,
            val lastSeen: String
    )
}
