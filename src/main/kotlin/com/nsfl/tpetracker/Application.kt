package com.nsfl.tpetracker

import com.fasterxml.jackson.databind.ObjectMapper
import com.nsfl.tpetracker.html.HTMLGenerator
import com.nsfl.tpetracker.model.pasta.CopyPastaRepository
import com.nsfl.tpetracker.model.player.PlayerRepository
import com.nsfl.tpetracker.model.position.Position
import com.nsfl.tpetracker.model.team.Team
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@RestController
@SpringBootApplication
@EnableScheduling
class Application {

    private val playerRepository = PlayerRepository()
    private val copyPastaRepository = CopyPastaRepository()
    private val htmlGenerator = HTMLGenerator()
    private var lastUpdateInfo = ""

    init {
        Thread {
            playerRepository.initialise()
            updatePlayers("Initial")
        }.start()
    }

    @Scheduled(cron = "0 0 10 * * MON-FRI")
    fun updatePlayersWeekday() {
        updatePlayers("Weekday")
    }

    @Scheduled(cron = "0 0 1,4,7,10,13,16,19,22 * * SUN,SAT")
    fun updatePlayersWeekend() {
        updatePlayers("Weekend")
    }

    private fun updatePlayers(type: String) {
        Logger.info("$type player update started.")
        val start = System.currentTimeMillis()
        playerRepository.update()
        lastUpdateInfo = "Last Update =>" +
                " Type: $type," +
                " Started At: ${Date(start)}," +
                " Duration: ${System.currentTimeMillis() - start} ms"
        Logger.info("$type player update finished.")
    }

    @RequestMapping("/last_update")
    fun getLastUpdateInformation(): String {
        return lastUpdateInfo
    }

    @RequestMapping("/players_json", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getActivePlayersJson(
            @RequestParam(required = false, defaultValue = "${Int.MIN_VALUE}") gt: Int,
            @RequestParam(required = false, defaultValue = "${Int.MAX_VALUE}") lt: Int
    ) = ObjectMapper().writeValueAsString(playerRepository.getAllPlayers().filter { it.currentTPE in gt..lt })

    @RequestMapping("/retired_players_json")
    fun getRetiredPlayersJson() = ObjectMapper().writeValueAsString(playerRepository.getRetiredPlayers())

    @RequestMapping("/error")
    fun getNextCopyPasta(request: HttpServletRequest) = htmlGenerator.createErrorPage(
            request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString(),
            copyPastaRepository.getNextCopyPasta()
    )

    @RequestMapping("/random_pasta")
    fun getRandomCopyPasta(request: HttpServletRequest) = htmlGenerator.createErrorPage(
            request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString(),
            copyPastaRepository.getRandomCopyPasta()
    )

    @RequestMapping("/")
    fun getIndex() = htmlGenerator.createIndexPage()

    @RequestMapping("/all_players")
    fun getAllPlayers() =
            htmlGenerator.createAllPlayersPage(playerRepository.getAllPlayers())

    @RequestMapping("/player")
    fun getPlayer(@RequestParam playerId: String) =
            playerRepository.getPlayer(playerId).let {
                htmlGenerator.createPlayerPage(it)
            }

    @RequestMapping("/team_stats")
    fun getTeamStats() = htmlGenerator.createTeamStatsPage(
            listOf(
                    Pair(Team.BALTIMORE_HAWKS, playerRepository.getBaltimoreHawksPlayers()),
                    Pair(Team.BERLIN_FIRE_SALAMANDERS, playerRepository.getBerlinFireSalamandersPlayers()),
                    Pair(Team.OSAKA_KAIJU, playerRepository.getOsakaKaijuPlayers()),
                    Pair(Team.COLORADO_YETI, playerRepository.getColoradoYetiPlayers()),
                    Pair(Team.CAPE_TOWN_CRASH, playerRepository.getCapeTownCrashPlayers()),
                    Pair(Team.SARASOTA_SAILFISH, playerRepository.getSarasotaSailfishPlayers()),
                    Pair(Team.YELLOWKNIFE_WRAITHS, playerRepository.getYellowknifeWraithsPlayers()),
                    Pair(Team.ARIZONA_OUTLAWS, playerRepository.getArizonaOutlawsPlayers()),
                    Pair(Team.AUSTIN_COPPERHEADS, playerRepository.getAustinCopperheadsPlayers()),
                    Pair(Team.HONOLULU_HAHALUA, playerRepository.getHonoluluHahaluaPlayers()),
                    Pair(Team.NEW_ORLEANS_SECOND_LINE, playerRepository.getNewOrleansSecondLinePlayers()),
                    Pair(Team.NEW_YORK_SILVERBACKS, playerRepository.getNewYorkSilverbacksPlayers()),
                    Pair(Team.ORANGE_COUNTY_OTTERS, playerRepository.getOrangeCountyOttersPlayers()),
                    Pair(Team.SAN_JOSE_SABERCATS, playerRepository.getSanJoseSabercatsPlayers())
            ),
            listOf(
                    Pair(Team.BONDI_BEACH_BUCCANEERS, playerRepository.getBondiBeachBuccaneersPlayers()),
                    Pair(Team.KANSAS_CITY_COYOTES, playerRepository.getKansasCityCoyotesPlayers()),
                    Pair(Team.PORTLAND_PYTHONS, playerRepository.getPortlandPythonsPlayers()),
                    Pair(Team.NORFOLK_SEAWOLVES, playerRepository.getNorfolkSeawolvesPlayers()),
                    Pair(Team.MINNESOTA_GREY_DUCKS, playerRepository.getMinnesotaGreyDucksPlayers()),
                    Pair(Team.TIJUANA_LUCHADORES, playerRepository.getTijuanaLuchadoresPlayers()),
                    Pair(Team.DALLAS_BIRDDOGS, playerRepository.getDallasBirddogsPlayers()),
                    Pair(Team.LONDON_ROYALS, playerRepository.getLondonRoyalsPlayers())
            )
    )

    @RequestMapping("/position_stats")
    fun getPositionStats() = htmlGenerator.createPositionStatsPage(
            listOf(
                    Pair(Position.QB, playerRepository.getQBPlayers()),
                    Pair(Position.RB, playerRepository.getRBPlayers()),
                    Pair(Position.WR, playerRepository.getWRPlayers()),
                    Pair(Position.TE, playerRepository.getTEPlayers()),
                    Pair(Position.OL, playerRepository.getOLPlayers()),
                    Pair(Position.DE, playerRepository.getDEPlayers()),
                    Pair(Position.DT, playerRepository.getDTPlayers()),
                    Pair(Position.LB, playerRepository.getLBPlayers()),
                    Pair(Position.CB, playerRepository.getCBPlayers()),
                    Pair(Position.S, playerRepository.getSPlayers()),
                    Pair(Position.KP, playerRepository.getKPPlayers())
            )
    )

    @RequestMapping("/activity_check_query")
    fun getActivityCheckQuery() = htmlGenerator.createActivityCheckQueryPage()

    @RequestMapping("/activity_check_result")
    fun getActivityCheckResult(@RequestParam startDate: String, @RequestParam endDate: String) =
            htmlGenerator.createActivityCheckResultPage(startDate, endDate, playerRepository.getAllPlayers())

    @RequestMapping("/baltimore_hawks")
    fun getBaltimoreHawksPlayers() = htmlGenerator.createTeamPage(
            Team.BALTIMORE_HAWKS,
            playerRepository.getBaltimoreHawksPlayers()
    )

    @RequestMapping("/berlin_fire_salamanders")
    fun getBerlinFireSalamandersPlayers() = htmlGenerator.createTeamPage(
            Team.BERLIN_FIRE_SALAMANDERS,
            playerRepository.getBerlinFireSalamandersPlayers()
    )

    @RequestMapping("/osaka_kaiju")
    fun getOsakaKaijuPlayers() = htmlGenerator.createTeamPage(
            Team.OSAKA_KAIJU,
            playerRepository.getOsakaKaijuPlayers()
    )

    @RequestMapping("/colorado_yeti")
    fun getColoradoYetiPlayers() = htmlGenerator.createTeamPage(
            Team.COLORADO_YETI,
            playerRepository.getColoradoYetiPlayers()
    )

    @RequestMapping("/cape_town_crash")
    fun getCapeTownCrashPlayers() = htmlGenerator.createTeamPage(
            Team.CAPE_TOWN_CRASH,
            playerRepository.getCapeTownCrashPlayers()
    )

    @RequestMapping("/yellowknife_wraiths")
    fun getYellowknifeWraithsPlayers() = htmlGenerator.createTeamPage(
            Team.YELLOWKNIFE_WRAITHS,
            playerRepository.getYellowknifeWraithsPlayers()
    )

    @RequestMapping("/arizona_outlaws")
    fun getArizonaOutlawsPlayers() = htmlGenerator.createTeamPage(
            Team.ARIZONA_OUTLAWS,
            playerRepository.getArizonaOutlawsPlayers()
    )

    @RequestMapping("/austin_copperheads")
    fun getAustinCopperheadsPlayers() = htmlGenerator.createTeamPage(
            Team.AUSTIN_COPPERHEADS,
            playerRepository.getAustinCopperheadsPlayers()
    )

    @RequestMapping("/new_orleans_second_line")
    fun getNewOrleansSecondLinePlayers() = htmlGenerator.createTeamPage(
            Team.NEW_ORLEANS_SECOND_LINE,
            playerRepository.getNewOrleansSecondLinePlayers()
    )

    @RequestMapping("/new_york_silverbacks")
    fun getNewYorkSilverbacksPlayers() = htmlGenerator.createTeamPage(
            Team.NEW_YORK_SILVERBACKS,
            playerRepository.getNewYorkSilverbacksPlayers()
    )

    @RequestMapping("/orange_county_otters")
    fun getOrangeCountyOttersPlayers() = htmlGenerator.createTeamPage(
            Team.ORANGE_COUNTY_OTTERS,
            playerRepository.getOrangeCountyOttersPlayers()
    )

    @RequestMapping("/san_jose_sabercats")
    fun getSanJoseSaberCatsPlayers() = htmlGenerator.createTeamPage(
            Team.SAN_JOSE_SABERCATS,
            playerRepository.getSanJoseSabercatsPlayers()
    )

    @RequestMapping("/honolulu_hahalua")
    fun getHonoluluHahaluaPlayers() = htmlGenerator.createTeamPage(
            Team.HONOLULU_HAHALUA,
            playerRepository.getHonoluluHahaluaPlayers()
    )

    @RequestMapping("/sarasota_sailfish")
    fun getSarasotaSailfishPlayers() = htmlGenerator.createTeamPage(
            Team.SARASOTA_SAILFISH,
            playerRepository.getSarasotaSailfishPlayers()
    )

    @RequestMapping("/bondi_beach_buccaneers")
    fun getBondiBeachBuccaneersPlayers() = htmlGenerator.createTeamPage(
            Team.BONDI_BEACH_BUCCANEERS,
            playerRepository.getBondiBeachBuccaneersPlayers()
    )

    @RequestMapping("/kansas_city_coyotes")
    fun getKansasCityCoyotesPlayers() = htmlGenerator.createTeamPage(
            Team.KANSAS_CITY_COYOTES,
            playerRepository.getKansasCityCoyotesPlayers()
    )

    @RequestMapping("/portland_pythons")
    fun getPortlandPythonsPlayers() = htmlGenerator.createTeamPage(
            Team.PORTLAND_PYTHONS,
            playerRepository.getPortlandPythonsPlayers()
    )

    @RequestMapping("/norfolk_seawolves")
    fun getNorfolkSeawolvesPlayers() = htmlGenerator.createTeamPage(
            Team.NORFOLK_SEAWOLVES,
            playerRepository.getNorfolkSeawolvesPlayers()
    )

    @RequestMapping("/minnesota_grey_ducks")
    fun getMinnesotaGreyDucksPlayers() = htmlGenerator.createTeamPage(
            Team.MINNESOTA_GREY_DUCKS,
            playerRepository.getMinnesotaGreyDucksPlayers()
    )

    @RequestMapping("/tijuana_luchadores")
    fun getTijuanaLuchadoresPlayers() = htmlGenerator.createTeamPage(
            Team.TIJUANA_LUCHADORES,
            playerRepository.getTijuanaLuchadoresPlayers()
    )

    @RequestMapping("/dallas_birddogs")
    fun getDallasBirddogsPlayers() = htmlGenerator.createTeamPage(
            Team.DALLAS_BIRDDOGS,
            playerRepository.getDallasBirddogsPlayers()
    )

    @RequestMapping("/london_royals")
    fun getLondonRoyalsPlayers() = htmlGenerator.createTeamPage(
            Team.LONDON_ROYALS,
            playerRepository.getLondonRoyalsPlayers()
    )

    @RequestMapping("/free_agents")
    fun getFreeAgents() = htmlGenerator.createTeamPage(
            Team.FREE_AGENTS,
            playerRepository.getFreeAgents()
    )

    @RequestMapping("/qb_prospects")
    fun getQBProspects() = htmlGenerator.createTeamPage(
            Team.QB_PROSPECTS,
            playerRepository.getQBProspects()
    )

    @RequestMapping("/rb_prospects")
    fun getRBProspects() = htmlGenerator.createTeamPage(
            Team.RB_PROSPECTS,
            playerRepository.getRBProspects()
    )

    @RequestMapping("/wr_prospects")
    fun getWRProspects() = htmlGenerator.createTeamPage(
            Team.WR_PROSPECTS,
            playerRepository.getWRProspects()
    )

    @RequestMapping("/te_prospects")
    fun getTEProspects() = htmlGenerator.createTeamPage(
            Team.TE_PROSPECTS,
            playerRepository.getTEProspects()
    )

    @RequestMapping("/ol_prospects")
    fun getOLProspects() = htmlGenerator.createTeamPage(
            Team.OL_PROSPECTS,
            playerRepository.getOLProspects()
    )

    @RequestMapping("/de_prospects")
    fun getDEProspects() = htmlGenerator.createTeamPage(
            Team.DE_PROSPECTS,
            playerRepository.getDEProspects()
    )

    @RequestMapping("/dt_prospects")
    fun getDTProspects() = htmlGenerator.createTeamPage(
            Team.DT_PROSPECTS,
            playerRepository.getDTProspects()
    )

    @RequestMapping("/lb_prospects")
    fun getLBProspects() = htmlGenerator.createTeamPage(
            Team.LB_PROSPECTS,
            playerRepository.getLBProspects()
    )

    @RequestMapping("/cb_prospects")
    fun getCBProspects() = htmlGenerator.createTeamPage(
            Team.CB_PROSPECTS,
            playerRepository.getCBProspects()
    )

    @RequestMapping("/s_prospects")
    fun getSProspects() = htmlGenerator.createTeamPage(
            Team.S_PROSPECTS,
            playerRepository.getSProspects()
    )

    @RequestMapping("/kp_prospects")
    fun getKPProspects() = htmlGenerator.createTeamPage(
            Team.KP_PROSPECTS,
            playerRepository.getKPProspects()
    )

    @RequestMapping("/retired_players")
    fun getRetiredPlayers() =
            htmlGenerator.createRetiredPlayersPage(playerRepository.getRetiredPlayers())

    @Controller
    class CustomErrorController : ErrorController {
        override fun getErrorPath() = "/error"
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
