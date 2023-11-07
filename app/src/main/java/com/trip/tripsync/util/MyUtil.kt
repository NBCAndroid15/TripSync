package com.trip.tripsync.util

class MyUtil {
    companion object{
        fun categoryIdToCategory(categoryId: Int) =
            when (categoryId) {
                12 -> "관광지"
                14 -> "문화시설"
                15 -> "축제"
                32 -> "숙박"
                38 -> "쇼핑"
                39 -> "음식점"
                else -> "관광지"
            }

        fun areaCodeToArea(areaCode: Int) =
            when (areaCode) {
                1 -> "서울"
                2 -> "인천"
                3 -> "대전"
                4 -> "대구"
                5 -> "광주"
                6 -> "부산"
                7 -> "울산"
                8 -> "세종특별자치시"
                31 -> "경기도"
                32 -> "강원특별자치도"
                33 -> "충청북도"
                34 -> "충청남도"
                35 -> "경상북도"
                36 -> "경상남도"
                37 -> "전라북도"
                38 -> "전라남도"
                39 -> "제주도"
                else -> "전체"
            }


    }
}