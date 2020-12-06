package net.nexcius.lolscraper

import net.nexcius.lolscraper.Role.*

data class Champion(val name: ChampionName, val id: Int, val roles: List<Role>, val aliases: List<ChampionName> = listOf())

object Champions {
    val ALL: List<Champion> = listOf(
        Champion("Aatrox", 266, listOf(TOP)),
        Champion("Ahri", 103, listOf(MIDDLE)),
        Champion("Akali", 84, listOf(MIDDLE, TOP)),
        Champion("Alistar", 12, listOf(SUPPORT)),
        Champion("Amumu", 32, listOf(JUNGLE)),
        Champion("Anivia", 34, listOf(MIDDLE)),
        Champion("Annie", 1, listOf(MIDDLE, SUPPORT)),
        Champion("Aphelios", 523, listOf(ADC)),
        Champion("Ashe", 22, listOf(ADC)),
        Champion("Aurelion Sol", 136, listOf(MIDDLE)),
        Champion("Azir", 268, listOf(MIDDLE)),
        Champion("Bard", 432, listOf(SUPPORT)),
        Champion("Blitzcrank", 53, listOf(SUPPORT), listOf("blitz")),
        Champion("Brand", 63, listOf(MIDDLE, SUPPORT)),
        Champion("Braum", 201, listOf(SUPPORT)),
        Champion("Caitlyn", 51, listOf(ADC)),
        Champion("Camille", 164, listOf(TOP)),
        Champion("Cassiopeia", 69, listOf(MIDDLE, ADC), listOf("cass")),
        Champion("Cho'Gath", 31, listOf(JUNGLE, TOP), listOf("cho")),
        Champion("Corki", 42, listOf(ADC, MIDDLE)),
        Champion("Darius", 122, listOf(TOP)),
        Champion("Diana", 131, listOf(MIDDLE)),
        Champion("Draven", 119, listOf(ADC)),
        Champion("Dr. Mundo", 36, listOf(JUNGLE, TOP), listOf("mundo")),
        Champion("Ekko", 245, listOf(JUNGLE, MIDDLE)),
        Champion("Elise", 60, listOf(JUNGLE)),
        Champion("Evelynn", 28, listOf(JUNGLE), listOf("eve")),
        Champion("Ezreal", 81, listOf(ADC, MIDDLE), listOf("ez")),
        Champion("Fiddlesticks", 9, listOf(JUNGLE), listOf("fiddle")),
        Champion("Fiora", 114, listOf(TOP)),
        Champion("Fizz", 105, listOf(MIDDLE)),
        Champion("Galio", 3, listOf(TOP, MIDDLE)),
        Champion("Gangplank", 41, listOf(TOP), listOf("gp")),
        Champion("Garen", 86, listOf(TOP)),
        Champion("Gnar", 150, listOf(TOP)),
        Champion("Gragas", 79, listOf(JUNGLE)),
        Champion("Graves", 104, listOf(JUNGLE)),
        Champion("Hecarim", 120, listOf(JUNGLE)),
        Champion("Heimerdinger", 74, listOf(MIDDLE), listOf("heimer")),
        Champion("Illaoi", 420, listOf(TOP)),
        Champion("Irelia", 39, listOf(TOP)),
        Champion("Ivern", 427, listOf(JUNGLE)),
        Champion("Janna", 40, listOf(SUPPORT)),
        Champion("Jarvan IV", 59, listOf(JUNGLE), listOf("jarvan")),
        Champion("Jax", 24, listOf(TOP, JUNGLE)),
        Champion("Jayce", 126, listOf(TOP)),
        Champion("Jhin", 202, listOf(ADC)),
        Champion("Jinx", 222, listOf(ADC)),
        Champion("Kai'Sa", 145, listOf(ADC)),
        Champion("Kalista", 429, listOf(ADC)),
        Champion("Karma", 43, listOf(SUPPORT)),
        Champion("Karthus", 30, listOf(MIDDLE, JUNGLE)),
        Champion("Kassadin", 38, listOf(MIDDLE)),
        Champion("Katarina", 55, listOf(MIDDLE)),
        Champion("Kayle", 10, listOf(TOP, MIDDLE)),
        Champion("Kayn", 141, listOf(JUNGLE)),
        Champion("Kennen", 85, listOf(TOP)),
        Champion("Kha'Zix", 121, listOf(JUNGLE), listOf("kha")),
        Champion("Kindred", 203, listOf(JUNGLE)),
        Champion("Kled", 240, listOf(TOP)),
        Champion("Kog'Maw", 96, listOf(ADC)),
        Champion("LeBlanc", 7, listOf(MIDDLE)),
        Champion("Lee Sin", 64, listOf(JUNGLE), listOf("lee")),
        Champion("Leona", 89, listOf(SUPPORT)),
        Champion("Lillia", 876, listOf(JUNGLE)),
        Champion("Lissandra", 127, listOf(MIDDLE)),
        Champion("Lucian", 236, listOf(ADC, MIDDLE)),
        Champion("Lulu", 117, listOf(SUPPORT, TOP)),
        Champion("Lux", 99, listOf(MIDDLE, SUPPORT)),
        Champion("Malphite", 54, listOf(TOP, JUNGLE, SUPPORT), listOf("malph")),
        Champion("Malzahar", 90, listOf(MIDDLE), listOf("malz")),
        Champion("Maokai", 57, listOf(JUNGLE, TOP)),
        Champion("Master Yi", 11, listOf(JUNGLE), listOf("yi")),
        Champion("Miss Fortune", 21, listOf(ADC), listOf("mf")),
        Champion("Wukong", 62, listOf(TOP)),
        Champion("Mordekaiser", 82, listOf(TOP), listOf("morde")),
        Champion("Morgana", 25, listOf(MIDDLE, SUPPORT)),
        Champion("Nami", 267, listOf(SUPPORT)),
        Champion("Nasus", 75, listOf(TOP, JUNGLE)),
        Champion("Nautilus", 111, listOf(SUPPORT, JUNGLE), listOf("naut")),
        Champion("Neeko", 518, listOf(MIDDLE, SUPPORT)),
        Champion("Nidalee", 76, listOf(JUNGLE)),
        Champion("Nocturne", 56, listOf(JUNGLE), listOf("noc")),
        Champion("Nunu & Willump", 20, listOf(JUNGLE), listOf("nunu")),
        Champion("Olaf", 2, listOf(JUNGLE, TOP)),
        Champion("Orianna", 61, listOf(MIDDLE), listOf("ori")),
        Champion("Ornn", 516, listOf(TOP)),
        Champion("Pantheon", 80, listOf(TOP, SUPPORT), listOf("panth")),
        Champion("Poppy", 78, listOf(TOP)),
        Champion("Pyke", 555, listOf(SUPPORT)),
        Champion("Qiyana", 246, listOf(MIDDLE)),
        Champion("Quinn", 133, listOf(TOP)),
        Champion("Rakan", 497, listOf(SUPPORT)),
        Champion("Rammus", 33, listOf(JUNGLE)),
        Champion("Rek'Sai", 421, listOf(JUNGLE)),
        Champion("Renekton", 58, listOf(TOP)),
        Champion("Rengar", 107, listOf(JUNGLE, TOP)),
        Champion("Riven", 92, listOf(TOP)),
        Champion("Rumble", 68, listOf(TOP)),
        Champion("Ryze", 13, listOf(MIDDLE)),
        Champion("Samira", 360, listOf(ADC)),
        Champion("Sejuani", 113, listOf(JUNGLE)),
        Champion("Senna", 235, listOf(ADC)),
        Champion("Seraphine", 147, listOf(SUPPORT, MIDDLE)),
        Champion("Sett", 875, listOf(TOP, MIDDLE)),
        Champion("Shaco", 35, listOf(JUNGLE, SUPPORT)),
        Champion("Shen", 98, listOf(TOP, JUNGLE)),
        Champion("Shyvana", 102, listOf(JUNGLE)),
        Champion("Singed", 27, listOf(TOP)),
        Champion("Sion", 14, listOf(TOP)),
        Champion("Sivir", 15, listOf(ADC)),
        Champion("Skarner", 72, listOf(JUNGLE)),
        Champion("Sona", 37, listOf(SUPPORT)),
        Champion("Soraka", 16, listOf(SUPPORT)),
        Champion("Swain", 50, listOf(ADC)),
        Champion("Sylas", 517, listOf(MIDDLE, TOP)),
        Champion("Syndra", 134, listOf(MIDDLE)),
        Champion("Tahm Kench", 223, listOf(SUPPORT), listOf("tahm")),
        Champion("Taliyah", 163, listOf(JUNGLE)),
        Champion("Talon", 91, listOf(MIDDLE)),
        Champion("Taric", 44, listOf(SUPPORT)),
        Champion("Teemo", 17, listOf(TOP, MIDDLE)),
        Champion("Thresh", 412, listOf(SUPPORT)),
        Champion("Tristana", 18, listOf(ADC), listOf("trist")),
        Champion("Trundle", 48, listOf(JUNGLE, TOP), listOf("trynd")),
        Champion("Tryndamere", 23, listOf(TOP)),
        Champion("Twisted Fate", 4, listOf(MIDDLE), listOf("tf")),
        Champion("Twitch", 29, listOf(ADC)),
        Champion("Udyr", 77, listOf(JUNGLE, TOP)),
        Champion("Urgot", 6, listOf(TOP)),
        Champion("Varus", 110, listOf(ADC)),
        Champion("Vayne", 67, listOf(ADC, TOP)),
        Champion("Veigar", 45, listOf(MIDDLE, SUPPORT)),
        Champion("Vel'Koz", 161, listOf(MIDDLE, SUPPORT)),
        Champion("Vi", 254, listOf(JUNGLE)),
        Champion("Viktor", 112, listOf(MIDDLE)),
        Champion("Vladimir", 8, listOf(TOP, MIDDLE), listOf("vlad")),
        Champion("Volibear", 106, listOf(JUNGLE, TOP), listOf("voli")),
        Champion("Warwick", 19, listOf(JUNGLE)),
        Champion("Xayah", 498, listOf(ADC)),
        Champion("Xerath", 101, listOf(MIDDLE)),
        Champion("Xin Zhao", 5, listOf(JUNGLE), listOf("xin")),
        Champion("Yasuo", 157, listOf(ADC, MIDDLE, TOP)),
        Champion("Yone", 777, listOf(MIDDLE, TOP)),
        Champion("Yorick", 83, listOf(TOP)),
        Champion("Yuumi", 350, listOf(SUPPORT)),
        Champion("Zac", 154, listOf(JUNGLE)),
        Champion("Zed", 238, listOf(MIDDLE, TOP)),
        Champion("Ziggs", 115, listOf(MIDDLE)),
        Champion("Zilean", 26, listOf(SUPPORT, MIDDLE)),
        Champion("Zoe", 142, listOf(SUPPORT, MIDDLE)),
        Champion("Zyra", 143, listOf(SUPPORT)),
    )

    fun inRole(role: Role): List<Champion> = ALL.filter { it.roles.contains(role) }
}
