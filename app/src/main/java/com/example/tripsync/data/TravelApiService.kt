package com.example.tripsync.data

import com.example.tripsync.model.festival.FestivalInfoResponse
import com.example.tripsync.model.travel.TravelInfoResponse
import com.example.tripsync.util.Constants.Companion.AUTH_HEADER
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TravelApiService {
    @GET("locationBasedList1")
    suspend fun getTravelInfoWithPosition(
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "TripSync",
        @Query("_type") type: String = "json",
        @Query("listYN") listYN: String = "Y",
        @Query("arrange") arrange: String = "A",
        @Query("mapX") mapX: Double,
        @Query("mapY") mapY: Double,
        @Query("radius") radius: Int = 10000,
        @Query("contentTypeId") contentTypeId: String = "12",
        @Query("serviceKey") serviceKey: String = AUTH_HEADER
    ): Response<TravelInfoResponse>
    @GET("searchKeyword1")
    suspend fun getTravelInfo(
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "TripSync",
        @Query("_type") type: String = "json",
        @Query("listYN") listYN: String = "Y",
        @Query("arrange") arrange: String = "R",
        @Query("keyword") keyword: String,
        @Query("contentTypeId") contentTypeId: String = "12",
        @Query("serviceKey") serviceKey: String = AUTH_HEADER
    ): Response<TravelInfoResponse>

    @GET("searchFestival1")
    suspend fun getFestivalInfo(
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "TripSync",
        @Query("_type") type: String = "json",
        @Query("listYN") listYN: String = "Y",
        @Query("arrange") arrange: String = "A",
        @Query("eventStartDate") eventStartDate: String = "20231001",
        @Query("serviceKey") serviceKey: String = AUTH_HEADER
    ): Response<FestivalInfoResponse>

}