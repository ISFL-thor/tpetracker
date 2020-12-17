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
                pageItem(url = "/baltimore_hawks", name = "Baltimore Hawks", imgSrc = "https://i.imgur.com/ZEuDUWh.png")
                pageItem(url = "/berlin_fire_salamanders", name = "Berlin Fire Salamanders", imgSrc ="https://cdn.discordapp.com/attachments/747977609454944339/754901898594746429/BERbanner.png")
                pageItem(url = "/chicago_butchers", name = "Chicago Butchers", imgSrc = "https://i.imgur.com/iVFr4uc.png")
                pageItem(url = "/colorado_yeti", name = "Colorado Yeti", imgSrc = "https://i.imgur.com/fWU19kH.png")
                pageItem(url = "/philadelphia_liberty", name = "Philadelphia Liberty", imgSrc = "https://i.imgur.com/aYEmbEm.png")
                pageItem(url = "/sarasota_sailfish", name = "Sarasota Sailfish", imgSrc = "https://cdn.discordapp.com/attachments/691424486926319616/692181074876563556/SARbanner.png")
                pageItem(url = "/yellowknife_wraiths", name = "Yellowknife Wraiths", imgSrc = "https://i.imgur.com/dJKYUhw.png")
                pageItem(url = "/arizona_outlaws", name = "Arizona Outlaws", imgSrc = "https://i.imgur.com/hagb6L3.png")
                pageItem(url = "/austin_copperheads", name = "Austin Copperheads", imgSrc = "https://i.imgur.com/mtaczoq.png")
                pageItem(url = "/honolulu_hahalua", name = "Honolulu Hahalua", imgSrc = "https://cdn.discordapp.com/attachments/691424711925563500/692401088829063168/HONbanner.png")
                pageItem(url = "/new_orleans_second_line", name = "New Orleans Second Line", imgSrc = "https://i.imgur.com/IY7NW3K.png")
                pageItem(url = "/new_york_silverbacks", name = "New York Silverbacks", imgSrc = "https://cdn.discordapp.com/attachments/747977675355979857/754901932727861288/NYSbanner.png")
                pageItem(url = "/orange_county_otters", name = "Orange County Otters", imgSrc = "https://i.imgur.com/8kqUlpH.png")
                pageItem(url = "/san_jose_sabercats", name = "San Jose SaberCats", imgSrc = "https://i.imgur.com/YTe2q5A.png")
            }
        }

        // dsfl team pages
        br {}
        container {
            row {
                pageItem(url = "/bondi_beach_buccaneers", name = "Bondi Beach Buccaneers", imgSrc = "https://cdn.discordapp.com/attachments/760202062595489824/777673794264236042/BBbanner.png")
                pageItem(url = "/london_royals", name = "London Royals", imgSrc = "https://i.imgur.com/oZSgFwV.png")
                pageItem(url = "/dallas_birddogs", name = "Dallas Birddogs", imgSrc = "https://i.imgur.com/kHc4zgX.png")
                pageItem(url = "/kansas_city_coyotes", name = "Kansas City Coyotes", imgSrc = "https://cdn.discordapp.com/attachments/712164068349444198/751206009422348348/KCCbanner.png")
                pageItem(url = "/portland_pythons", name = "Portland Pythons", imgSrc = "https://i.imgur.com/rgXXbc8.png")
                pageItem(url = "/norfolk_seawolves", name = "Norfolk SeaWolves", imgSrc = "https://i.imgur.com/4ZHdLpl.png")
                pageItem(url = "/minnesota_grey_ducks", name = "Minnesota Grey Ducks", imgSrc = "https://i.imgur.com/ksywLuJ.png")
                pageItem(url = "/tijuana_luchadores", name = "Tijuana Luchadores", imgSrc = "https://i.imgur.com/ZFILBNT.png")
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
