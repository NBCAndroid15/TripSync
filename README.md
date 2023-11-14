<div align="left">
<img width="200" src="https://lab.ssafy.com/s07-webmobile4-sub2/S07P12D101/uploads/0a9e04f048a227e4d11bb069518f5ad4/rwm_logo_white.jpg">
</div>
<br/>

# ✈️TripSync

## TripSync - 여행을 동기화 하다!

**TripSync** 프로젝트는 Android 모바일로 제공되는 여행 계획 및 동선을 작성하고 친구와 공유 할 수 있는 서비스를 제공하는 어플리케이션 입니다.

## 주요 타겟층

- 친구들과 여행을 가기 전에 성향이 다른 친구를 철저하게 대비하고 싶은 분
	-  여행 논쟁에 항상 화두 되는 여행 스타일이 다른 친구, 계획을 좋아하지 않는 친구도 앱 하나로 간편하게 여행 스케쥴에 동참할 수 있어 본격적인 여행을 가기 전부터 발생하는 트러블을 줄일 수 있습니다.

- 혼자서 떠나는 여행에 기록을 남기고 싶은 혼여행족
	- 나만의 여행 동선을 만들어 가고싶은 곳을 마구 설정해보고, 여행 후에는 그때를 떠올리며 어떤 곳을 갔는지 무엇을 먹었는지 나를 위한 선물은 무엇이었는지 오래오래 보관하고 회상할 수 있습니다.


# 📃 Description

서비스 주요 기능



서비스 전체 기능

<ul>
	<li>챌린지 러닝 기능 - 챌린지마다 러닝 기간, 시간, 목표 등을 정해서 정해진 시간에만 러닝 가능</li>
	<li>러닝 기능 - 러닝을 뛰는 실시간 경로를 지도에 표기, 기록 표기</li>
	<li>러닝 기록 기능 - 러닝을 완료하고 경로 사진과 기록 등을 자동으로 등록 되게 하여 기록을 서로 공유할 수 있음. 누적 기록을 볼 수 있고 세부 러닝 기록을 캘린더 형식으로 볼 수 있음</li>
	<li>랭킹 기능 - 챌린지 내에서 거리별, 시간별 등으로 정렬하여 챌린지 팀원 랭킹을 볼 수 있고 전체 랭킹에서 유저별 랭킹을 볼 수 있음</li>
	<li>챌린지 내 소통 기능 - 게시판 기능을 통하여 챌린지 팀원들끼리 소통 가능</li>
	<li>업적 기능 - 업적 달성 시 뱃지를 지급</li>
	<li>대회 기능 - 시즌 내 기간 동안 달린 누적 거리로 랭킹 계산, 포인트와 뱃지 보상 지급</li>
	<li>추천 기능 - 러닝 완료 후 기록을 사용자들에게 추천할 수 있음</li>
	<li>스크랩 기능 - 사용자들이 올린 추천 경로를 스크랩을 할 수 있고 추천 경로를 러닝 화면에 나타내어 뛸 수 있음</li>
	<li>날씨 기능 - 최근 30분 이내의 예보를 통해 현재 날씨 확인 가능</li>
	<li>TTS 기능 - 러닝 시작, 정지 시, 그리고 1km마다 소요 시간을 음성으로 알려줌</li>
	<li>워치 연동 기능 - 워치와 로그인을 연동 가능</li>
	<li>워치 러닝 기능 - 워치로도 러닝 기능이 가능</li>
	<li>신고 기능 - 불편한 게시물을 신고 할 수 있음</li>
	<li>문의 기능 - 문의할 수 있음</li>
</ul>
<br/>
<br/>

## 📝 Design

> <h3><font color="green">전체 기술 스택</font></h3>

> ![image](./img/6.전체기술스택.png) > <br/>

> <h3><font color="green">전체 시스템 구조</font></h3>

> ![image](./img/7.전체시스템구조.png) > <br/>

> <h3><font color="green">ERD 다이어그램</font></h3>

> ![1](./img/8.ERD다이어그램.png) > [링크](https://www.erdcloud.com/d/uoAsmnv3gyCugnxno) 바로가기
> <br/>

> <h3><font color="green">요구사항 명세서</font></h3>

> ![image](./img/9.요구사항명세서.gif) <br/>

> <br/>
> <br/>

> <h3><font color="green">API 명세서</font></h3>

> ![image](./img/10.API명세서.gif) <br/>

> <br/>
> <br/>

## 📱 Android

> <h3><font color="green">사용한 라이브러리</font></h3>

| Name             | Description                                  |
| ---------------- | -------------------------------------------- |
| Data Layer API   | Wear OS 네트워크 통신 라이브러리             |
| Navigation       | 프래그먼트 전환 라이브러리                   |
| Glide            | 이미지 로딩 라이브러리                       |
| ViewModel        | 수명주기 고려 데이터를 저장, 관리 라이브러리 |
| Coroutine        | 비동기 처리 라이브러리                       |
| Coroutine Flow   | 비동기 데이터 스트림                         |
| TedPermission    | 안드로이드 권한 라이브러리                   |
| Retrofit         | HTTP 통신 라이브러리                         |
| SimpleRatingBar  | RatingBar 커스텀 라이브러리                  |
| Dagger Hilt      | 의존성 주입 라이브러리                       |
| Circle ImageView | 이미지뷰 라이브러리                          |
| Google Auth      | 구글 소셜 로그인                             |
| Naver            | 네이버 소셜 로그인                           |
| KaKao            | 카카오 소셜 로그인                           |
| Lottie           | 안드로이드 애니메이션 라이브러리             |
| GoogleMap        | 구글 지도 라이브러리                         |
| FCM              | 파이어베이스 푸시 알림                       |
| Paging 3         | 안드로이드 페이징 라이브러리                 |
| Mp Android Chart | 안드로이드 차트 통계 라이브러리              |
| Room             | 안드로이드 DB 라이브러리                     |
| Weather Api      | 공공데이터포털 기상청 날씨 라이브러리        |
| CalendarView     | 캘린더 라이브러리                            |

<br/>

## 아키텍처 구조
> ![서비스소개](./img/11.아키텍처구조.png) > <br/> > <br/>

## 👨‍👦‍👦 Team Member

> <h3><font color="red">Android Part</font></h3>

![image](./img/14.팀원역할1.png)

> <br/>

> <h3><font color="red">Back-End Part</font></h3>

![image](./img/15.팀원역할2.png)

> <br/>
> <br/>
