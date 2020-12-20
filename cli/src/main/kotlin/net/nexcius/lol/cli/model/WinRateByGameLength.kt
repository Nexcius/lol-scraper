package net.nexcius.lol.cli.model

data class WinRateByGameLength(
    val `0-25`: Float?,
    val `25-30`: Float?,
    val `30-35`: Float?,
    val `35-40`: Float?,
    val `40+`: Float?) {

    companion object {
        fun fromMap(percValues: Map<String, Float?>) = WinRateByGameLength(
            percValues["0-25"],
            percValues["25-30"],
            percValues["30-35"],
            percValues["35-40"],
            percValues["40+"]
        )

        fun fromList(percValues: List<Float?>) = WinRateByGameLength(
            percValues.getOrNull(0),
            percValues.getOrNull(1),
            percValues.getOrNull(2),
            percValues.getOrNull(3),
            percValues.getOrNull(4)
        )
    }
}