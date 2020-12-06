package net.nexcius.lolscraper

import java.util.*

fun resolveChampsFromArgs(role: Role, include: String, exclude: String): SortedSet<ChampionName> {
    val includedChamps = include.split(",").filter(String::isNotEmpty).map(::resolveChampionName)
    val excludedChamps = exclude.split(",").filter(String::isNotEmpty).map(::resolveChampionName)

    return (Champions.inRole(role).map { it.name } + includedChamps - excludedChamps).toSortedSet()
}

fun resolveChampionName(name: String): ChampionName {
    val directMatch = Champions.ALL
        .map { it.name to levenshtein(it.name.toLowerCase(), name.toLowerCase()) }
        .filter { it.second <= 1 }
        .sortedByDescending { it.second }
        .map { it.first }
        .firstOrNull()

    val aliasMatch = Champions.ALL
        .flatMap { champ -> champ.aliases.map { alias -> champ.name to alias } }
        .find { it.second.toLowerCase() == name.toLowerCase() }
        ?.first

    val result = directMatch ?: aliasMatch
    if (result == null) {
        println("Unable to find champion <$name>")
        kotlin.system.exitProcess(1)
    }

    return result
}

fun levenshtein(
    s: String, t: String,
    charScore: (Char, Char) -> Int = { c1, c2 -> if (c1 == c2) 0 else 1 }
): Int {

    // Special cases
    if (s == t) return 0
    if (s == "") return t.length
    if (t == "") return s.length

    val initialRow: List<Int> = (0 until t.length + 1).map { it }.toList()
    return (0 until s.length).fold(initialRow, { previous, u ->
        (0 until t.length).fold(mutableListOf(u + 1), { row, v ->
            row.add(
                minOf(
                    row.last() + 1,
                    previous[v + 1] + 1,
                    previous[v] + charScore(s[u], t[v])
                )
            )
            row
        })
    }).last()
}
