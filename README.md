<!-- Table of Contents -->
# :notebook_with_decorative_cover: Table of Contents
- [프로젝트 소개](#star2-프로젝트-소개)
  * [핵심기능](#dart-핵심기능)
  * [스크린샷](#camera-스크린샷)
  * [기술스택](#space_invader-기술스택)
  * [아키텍처](#building_construction-아키텍처)
- [프로젝트 구현 및 이슈해결 과정](#fountain_pen-프로젝트-구현-및-이슈-해결-과정)
  * [요구사항 분석](#2nd_place_medal-요구사항-분석)
  * [api 명세서](#bookmark_tabs-api-명세서)
  * [erd](#book-erd)
  * [이슈 해결 과정](#checkered_flag-이슈-해결-과정)
- [로드맵](#compass-로드맵)
- [Contact](#handshake-contact)

<!-- 프로젝트 소개 -->
## :star2: 프로젝트 소개

<!-- 핵심기능 -->
### :dart: 핵심기능

- OpenAI API 를 활용한 AI 멘토링 및 딥다이브 웹 애플리케이션 백엔드 RestAPI입니다.
- 멘토링 시 주제별 전문가의 멘토링을 받을 수 있습니다.
- 질문에 대한 멘토링으로 진행되며, 프롬프트 엔지니어링으로 보다 정확하고 이해하기 쉽게 예시를 들어 해당 주제의 전문가 답변을 제공합니다.
- 멘토링 답변에 대한 탐구할만한 연관질문 생성기능(DEEP DIVE)을 제공합니다.
- 딥다이브 기능으로 주제에 연관된 여러 질문을 생성해줌으로써 주제에 대한 깊은 이해와 탐구가 가능하도록 합니다.

<!-- 스크린샷 -->
### :camera: 스크린샷
<div align="center"> 
  <img src="https://github.com/soonhankwon/dev-space-x-backend/assets/113872320/0a7a7f3b-b7d0-4e62-a6e0-bef64cf2149e" width="200" height="300" alt="screenshot"/>
  <img src="https://github.com/soonhankwon/dev-space-x-backend/assets/113872320/114c4e79-fef2-4b56-82a2-23bcfa90c9dd" width="200" height="300" alt="screenshot"/>
  <img src="https://github.com/soonhankwon/dev-space-x-backend/assets/113872320/d8e3e9b6-5181-4843-a1b7-a26bfdd9d2b4" width="200" height="300" alt="screenshot"/>
</div>

<!-- 기술스택 -->
### :space_invader: 기술스택

- Java 17 Amazon Corretto
- SpringBoot 2.7.15
- theokanning.openai-gpt3-java 0.16.0
- Spring Data JPA
- Spring Security 5.7.10
- Spring WebSocket 5.3.29
- Swagger 3.0.0
- MySQL 8.0.33

#### :computer: DevOps
 
- AWS VPC
- AWS EC2
- AWS RDS
- Docker
- Github Actions

<!-- 아키텍처 -->
### :building_construction: 아키텍처
<div align="center"> 
<img src="https://github.com/soonhankwon/tech-interview-defense/assets/113872320/ab99c965-e925-4de5-bfcc-a8b5480bfe0d" width="540" height="330">
</div>

<!-- 프로젝트 구현 및 이슈 해결 과정 -->
## :fountain_pen: 프로젝트 구현 및 이슈 해결 과정

<!-- 요구사항 분석 -->
### :2nd_place_medal: 요구사항 분석
- 해커톤 기업 주제: AI 멘토
- 요구사항 1: Java / JavaScript / Kotlin / React / Next.js / Node.js / Nest.js / Spring / CS 주제별로 질문 / 대답이 가능한 AI 멘토를 구현
- 요구사항 2: AI 멘토에게 궁금한 것을 물어보면 이해할 수 있는 답변을 해야함
- 요구사항 3: AI 멘토가 먼저 면접질문등을 물어볼 수 있으며, 대답에 맞추어 꼬리질문을 할 수 있어야 함

<details>
<summary>요구사항 분석 1, 2 - click</summary>
<div markdown="1">

```plain
- 해당 사항은 주제별로 정확하면서, 디테일한 멘토링을 해줘야하는 요구사항으로 파악했습니다.
  - 첫번째, 주제별이라는 것에서 사용자가 주제를 선택하고 주제에 대한 멘토링룸을 만들면되겠다고 분석했습니다.
  - 두번째, 정확하면서, 이해할수있는 멘토링은 멘토링룸의 주제에 대한 전문가 ROLE을 AI에게 설정해주면 될것이라고 분석했습니다.
  
예를 들면 사용자가 자바주제의 멘토링룸에서 스트링에 대해서 설명해주세요.라는 질문인데 자바스크립트의 스트링에 대한 답변을 받는다면 안됩니다.
이부분은 자바 주제의 전문가 ROLE을 AI에게 프롬프트 엔지니어링 해줍니다.
또한, 개인적으로 최대한 적합한 예시가 있을 경우 답변이 이해하기 쉽다는 것을 경험했습니다.
따라서, 최대한 이해하기 좋은 예시를 들어 답변하도록 프롬프트 엔지니어링해주면 정확하면서 좋은 멘토링 기능을 제공할 수 있을것이라고 분석했습니다.
``` 
</div>
</details>

<details>
<summary>요구사항 분석 3 - 해당 요구사항은 해커톤에서 시연 완료된 기능이지만 이전 질문들을 반복하는 경우가 발생하여 해당 기능은 Deprecated & 개선중입니다.</summary>
<div markdown="1">

```plain
- 사용자는 단순히 AI 멘토에게 질문에 대한 답변을 원할수도 있고 인터뷰 질문을 받는 것을 원할 수도 있습니다.
- 인터뷰 질문을 받는 것을 원하지 않는 사용자도 분명히 있을 것이라고 생각해서 채팅방을 만들때 이것을 사용자가 옵션으로 선택하도록 했습니다.  
- 기술면접 디펜스 옵션을 만들어 실제 인터뷰 상황과 가깝게 AI 멘토가 먼저 질문을 하고 꼬리질문을 통해 사용자를 평가하는데 초점을 맞췄습니다.
- 부가적인 재미를 위해서 디펜스 게임의 요소를 넣으면 좋겠다고 분석했습니다.
``` 
</div>
</details>

<!-- API 명세서 -->
### :bookmark_tabs: api 명세서
- [Swagger API 명세서](http://43.202.192.55/swagger-ui/index.html#/)

<!-- ERD -->
### :book: erd
- [ERD](http://43.202.192.55/swagger-ui/index.html#/)

<!-- 이슈 해결 과정 -->
### :checkered_flag: 이슈 해결 과정
- [딥다이브 질문생성시 AI의 상황에 맞지 않는 응답 다수 발생]
  * [프롬프트 엔지니어링의 지속적인 학습방법으로 개선](https://www.notion.so/AI-98e141417a1745a780ca57626429b144?pvs=4)
- [멘토링 대기 Latency 약 10~15초]
  * [GPT StreamChatCompletion 활용한 멘토링 응답대기 Latency 개선](https://www.notion.so/GPT-Stream-Completion-4dd198e0fe0745eab3ca1dad284c4e67?pvs=4)
- [GPT StreamChatCompletion 응답시간 개선]
  * [버퍼사용 - 약 1.2초, 개선율 6.87%](https://www.notion.so/GPT-StreamChatCompletion-5d83e24ec90b4e7282b0c310ea38690c?pvs=4)
- [멘토링 서비스시 채팅(Chat) 조회 DB 콜 개선]
  * [ConcurrentHashMap 활용 - 불필요 DB콜 개선, 약 4.6초, 개선율 약 26.93%](https://www.notion.so/Chat-DB-ConcurrentHashMap-648ad21769d94d7ba61e9036f016de19?pvs=4)
- [NCloud 크레딧 소진으로 인한 AWS 이전 - 클라우드 인프라 구축]
- [RestAPI로 리팩토링 - 프론트엔드 React를 활용한 VIEW로직 백엔드에서 분리] 

<!-- 로드맵 -->
## :compass: 로드맵

* [x] 멘토링모드 OpenAPI 스트리밍 기능 활용
* [x] 멘토링 답변에 대한 연관질문 생성 기능 구현
* [x] NCloud 에서 AWS로 서버이전
* [x] Backend RestAPI로 리팩토링 + 프론트 React로 렌더링 작업

<!-- Contact -->
## :handshake: Contact

Email - soonable@gmail.com
