package com.example.navitime_challenge.network

import com.example.navitime_challenge.domain.Coord
import com.example.navitime_challenge.domain.Route
import com.example.navitime_challenge.database.DatabaseRoute
import com.squareup.moshi.JsonClass
import kotlin.random.Random

@JsonClass(generateAdapter = true)
data class NavitimeRouteContainer(
    val routes: List<NavitimeRoute>,
    val goaltime: String)

@JsonClass(generateAdapter = true)
data class NavitimeRoute(
    val id: Int,
    val type: String,
    val name: String?,
    val coord: Coord?,
    val move: String?,
    val from_time: String?,
    val to_time: String?,
    val time: Int?,
    val distance: Int?)

fun NavitimeRouteContainer.asDomainModel(): List<Route> {
    return routes.map {
        Route(
            id = it.id,
            type = it.type,
            name = it.name,
            coord = it.coord,
            move = it.move,
            from_time = it.from_time,
            to_time = it.to_time,
            time = it.time,
            distance = it.distance)
    }
}

fun NavitimeRouteContainer.asDatabaseModel(): List<DatabaseRoute> {
    return routes.map {
        DatabaseRoute(
            id = it.id,
            type = it.type,
            name = it.name,
            lat = it.coord?.lat,
            lon = it.coord?.lon,
            move = it.move,
            from_time = it.from_time,
            to_time = it.to_time,
            time = it.time,
            distance = it.distance)
    }
}
