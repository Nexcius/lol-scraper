package net.nexcius.lol.cli.model

import net.nexcius.lol.cli.ChampionName

data class BaseData(val championName: ChampionName, val winRate: Float?, val pickRate: Float?, val winRateByGameLength: WinRateByGameLength)
