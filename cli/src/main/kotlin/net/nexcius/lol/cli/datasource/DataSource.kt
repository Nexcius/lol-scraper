package net.nexcius.lol.cli.datasource

import net.nexcius.lol.cli.ChampionName
import net.nexcius.lol.cli.Role
import net.nexcius.lol.cli.model.BaseData
import net.nexcius.lol.cli.model.MatchUpData

interface DataSource {
    fun getMatchUps(championName: ChampionName, role: Role): MatchUpData
    fun getBaseData(championName: ChampionName, role: Role): BaseData
}
