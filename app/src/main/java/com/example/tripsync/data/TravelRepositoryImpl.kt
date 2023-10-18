package com.example.tripsync.data

import com.example.tripsync.model.Travel
import com.example.tripsync.util.MyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TravelRepositoryImpl {
    private val api = RetrofitInstance.api

    suspend fun getTravelInfoWithPosition(pageNo: Int, mapX: Double, mapY: Double) = withContext(Dispatchers.IO) {
        try {
            val response = api.getTravelInfoWithPosition(pageNo = pageNo, mapX = mapX, mapY = mapY)

            if (response.isSuccessful) {
                response.body()!!.response.body.items.item.map {
                    Travel(
                        imageUrl = it.firstimage,
                        title = it.title,
                        addr = it.addr1,
                        mapX = it.mapx.toDouble(),
                        mapY = it.mapy.toDouble(),
                        contentId = it.contentid.toInt(),
                        contentTypeId = it.contenttypeid.toInt(),
                        category = MyUtil.categoryIdToCategory(it.contenttypeid.toInt()),
                        area = MyUtil.areaCodeToArea(it.areacode.toInt()),
                        tel = it.tel
                    )
                }
            }
            else {
                listOf()
            }
        } catch (e: Exception) {
            listOf()
        }
    }
    suspend fun getTravelInfo(pageNo: Int, keyword: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.getTravelInfo(pageNo = pageNo, keyword = keyword)

            if (response.isSuccessful) {
                response.body()!!.response.body.items.item.map {
                    Travel(
                        imageUrl = it.firstimage,
                        title = it.title,
                        addr = it.addr1,
                        mapX = it.mapx.toDouble(),
                        mapY = it.mapy.toDouble(),
                        contentId = it.contentid.toInt(),
                        contentTypeId = it.contenttypeid.toInt(),
                        category = MyUtil.categoryIdToCategory(it.contenttypeid.toInt()),
                        area = MyUtil.areaCodeToArea(it.areacode.toInt()),
                        tel = it.tel
                    )
                }
            }
            else {
                listOf()
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    suspend fun getFestivalInfo(pageNo: Int) = withContext(Dispatchers.IO) {
        try {
            val response = api.getFestivalInfo(pageNo = pageNo)

            if (response.isSuccessful) {
                response.body()!!.response.body.items.item.map {
                    Travel(
                        imageUrl = it.firstimage,
                        title = it.title,
                        addr = it.addr1,
                        mapX = it.mapx.toDouble(),
                        mapY = it.mapy.toDouble(),
                        contentId = it.contentid.toInt(),
                        contentTypeId = it.contenttypeid.toInt(),
                        category = MyUtil.categoryIdToCategory(it.contenttypeid.toInt()),
                        area = MyUtil.areaCodeToArea(it.areacode.toInt()),
                        tel = it.tel,
                        startDate = it.eventstartdate,
                        endDate = it.eventenddate
                    )
                }
            }
            else {
                listOf()
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    suspend fun getTravelDetailInfo(contentId: Int, contentTypeId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = api.getDetailInfo(contentId = contentId, contentTypeId = contentTypeId)

            if (response.isSuccessful) {
                val data = response.body()?.response?.body?.items?.item

                if (data.isNullOrEmpty()) {
                    ""
                } else {
                    data[0].overview
                }
            } else {
                ""
            }

        } catch (e: Exception) {
            ""
        }
    }
}