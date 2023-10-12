package com.example.tripsync.util

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
                9 -> "경기도"
                10 -> "강원특별자치도"
                11 -> "충청북도"
                12 -> "충청남도"
                13 -> "경상북도"
                14 -> "경상남도"
                15 -> "전라북도"
                16 -> "전라남도"
                17 -> "제주도"
                else -> ""
            }


    }
}