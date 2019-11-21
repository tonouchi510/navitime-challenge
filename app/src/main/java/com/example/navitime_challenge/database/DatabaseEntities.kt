package com.example.navitime_challenge.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.navitime_challenge.domain.*


@Entity(tableName = "routes")
data class DatabaseRoute constructor(
    @PrimaryKey
    val id: Long,
    val type: String,
    val name: String?,
    val lat: Double?,
    val lon: Double?,
    val move: String?,
    val from_time: String?,
    val to_time: String?,
    val time: Int?,
    val distance: Int?)


fun List<DatabaseRoute>.asDomainModel(): List<Route> {
    return map {
        Route(
            type = it.type,
            name = it.name,
            coord = Coord(
                lat = it.lat,
                lon = it.lon
            ),
            move = it.move,
            from_time = it.from_time,
            to_time = it.to_time,
            time = it.time,
            distance = it.distance)
    }
}
