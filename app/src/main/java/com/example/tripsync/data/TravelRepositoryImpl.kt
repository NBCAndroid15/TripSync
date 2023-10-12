package com.example.tripsync.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TravelRepositoryImpl {
    private val api = RetrofitInstance.api

    suspend fun getTravelInfo(pageNo: Int, keyword: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.getTravelInfo(pageNo = pageNo, keyword = keyword)

            if (response.isSuccessful) {

            }
        } catch (e: Exception) {

        }
    }
}