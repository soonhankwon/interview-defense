<!-- Table of Contents -->
# :notebook_with_decorative_cover: Table of Contents
- [프로젝트 소개](#star2-프로젝트-소개)
  * [핵심기능](#dart-핵심기능)
  * [스크린샷](#camera-스크린샷)
  * [기술스택](#space_invader-기술스택)
  * [아키텍처](#building_construction-아키텍처)
- [프로젝트 구현 및 이슈해결 과정](#fountain_pen-프로젝트-구현-및-이슈-해결-과정)
  * [api 명세서](#bookmark_tabs-api-명세서)
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
- 딥다이브 기능으로 주제에 연관된 여러 질문을 생성해줌으로써 보다 깊은 이해와 탐구가 가능하도록 합니다. 
- 멘토링 모드는 StreamChatCompletion 기능이 구현되어 있습니다.

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

<!-- API 명세서 -->
### :bookmark_tabs: api 명세서
- http://43.202.192.55/

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
