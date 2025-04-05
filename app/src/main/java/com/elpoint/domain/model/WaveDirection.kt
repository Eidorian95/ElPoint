package com.elpoint.domain.model

enum class Direction(val abbreviation: String) {
    N("N"), NE("NE"), E("E"), SE("SE"),
    S("S"), SW("SO"), W("O"), NW("NO"),
    NA("N/A");

    companion object {
        fun fromDegrees(degrees: Int): Direction {
            return when (degrees) {
                in 0..22, in 338..360 -> N
                in 23..67 -> NE
                in 68..112 -> E
                in 113..157 -> SE
                in 158..202 -> S
                in 203..247 -> SW
                in 248..292 -> W
                in 293..337 -> NW
                else -> NA
            }
        }
    }
}