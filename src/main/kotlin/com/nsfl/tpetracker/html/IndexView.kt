package com.nsfl.tpetracker.html

import kotlinx.html.*

private fun DIV.pageItem(url: String, name: String, wide: Boolean = false, imgSrc: String? = null, xlMargin: Int = if (wide) 6 else 4) {
    a(classes = "col-md-6 mb-4 col-xl-${xlMargin}", href = url) {
        card(classes = "border-0 shadow") {
            if (imgSrc != null) {
                cardImgTop(src = imgSrc) {}
            }
            cardBody(classes = "text-center") {
                if (wide)
                    br {}
                cardTitle(classes = "mb-0") { +name }
                if (wide)
                    br {}
            }
        }
    }
}

fun HTML.indexView() {
    head {
        title { +"TPE Tracker" }
        link(href = "https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css", rel = "stylesheet") {}
        script(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.bundle.min.js") {}
    }
    body {

        // main pages
        br {}
        br {}
        container {
            row {
                pageItem(url = "/all_players", name = "All Players", wide = true)
                pageItem(url = "/team_stats", name = "Team Stats", wide = true)
                pageItem(url = "/position_stats", name = "Position Stats", wide = true)
                pageItem(url = "/activity_check_query", name = "Activity Check", wide = true)
            }
        }

        // nsfl team pages
        br {}
        container {
            row {
                pageItem(url = "/baltimore_hawks", name = "Baltimore Hawks", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/BALbanner.png")
                pageItem(url = "/berlin_fire_salamanders", name = "Berlin Fire Salamanders", imgSrc ="https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/BERbanner.png")
                pageItem(url = "/osaka_kaiju", name = "Osaka Kaiju", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/OSAbanner.png")
                pageItem(url = "/colorado_yeti", name = "Colorado Yeti", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/COLbanner.png")
                pageItem(url = "/cape_town_crash", name = "Cape Town Crash", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/CTCbanner.png")
                pageItem(url = "/sarasota_sailfish", name = "Sarasota Sailfish", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/SARbanner.png")
                pageItem(url = "/yellowknife_wraiths", name = "Yellowknife Wraiths", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/YKWbanner.png")
                pageItem(url = "/arizona_outlaws", name = "Arizona Outlaws", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/AZbanner.png")
                pageItem(url = "/austin_copperheads", name = "Austin Copperheads", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/AUSbanner.png")
                pageItem(url = "/honolulu_hahalua", name = "Honolulu Hahalua", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/HONbanner.png")
                pageItem(url = "/new_orleans_second_line", name = "New Orleans Second Line", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/NOLAbanner.png")
                pageItem(url = "/new_york_silverbacks", name = "New York Silverbacks", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/NYSbanner.png")
                pageItem(url = "/orange_county_otters", name = "Orange County Otters", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/OCObanner.png")
                pageItem(url = "/san_jose_sabercats", name = "San Jose SaberCats", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/ISFL/SJSbanner.png")
            }
        }

        // dsfl team pages
        br {}
        container {
            row {
                pageItem(url = "/bondi_beach_buccaneers", name = "Bondi Beach Buccaneers", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/DSFL/BBbanner.png")
                pageItem(url = "/london_royals", name = "London Royals", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/DSFL/LDNbanner.png")
                pageItem(url = "/dallas_birddogs", name = "Dallas Birddogs", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/DSFL/DALbanner.png")
                pageItem(url = "/kansas_city_coyotes", name = "Kansas City Coyotes", imgSrc = "https://i.postimg.cc/44v59VfV/KCCbanner.png")
                pageItem(url = "/portland_pythons", name = "Portland Pythons", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/DSFL/PDXbanner.png")
                pageItem(url = "/norfolk_seawolves", name = "Norfolk SeaWolves", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/DSFL/NORbanner.png")
                pageItem(url = "/minnesota_grey_ducks", name = "Minnesota Grey Ducks", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/DSFL/MIN_banner.png")
                pageItem(url = "/tijuana_luchadores", name = "Tijuana Luchadores", imgSrc = "https://forums.sim-football.com/images/smilies/isfl/banners/DSFL/TIJbanner.png")
            }
        }

        // player category pages
        br {}
        container {
            row {
                pageItem(url = "/free_agents", name = "Free Agents")
                pageItem(url = "/qb_prospects", name = "QB Prospects")
                pageItem(url = "/rb_prospects", name = "RB Prospects")
                pageItem(url = "/wr_prospects", name = "WR Prospects")
                pageItem(url = "/te_prospects", name = "TE Prospects")
                pageItem(url = "/ol_prospects", name = "OL Prospects")
                pageItem(url = "/de_prospects", name = "DE Prospects")
                pageItem(url = "/dt_prospects", name = "DT Prospects")
                pageItem(url = "/lb_prospects", name = "LB Prospects")
                pageItem(url = "/cb_prospects", name = "CB Prospects")
                pageItem(url = "/s_prospects", name = "S Prospects")
                pageItem(url = "/kp_prospects", name = "K/P Prospects")
                pageItem(url = "/retired_players", name = "Retired Players")
            }
        }

        br {}
        br {}
    }
}
