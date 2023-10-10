<!-- Table of Contents -->
# :notebook_with_decorative_cover: Table of Contents

- [About the Project](#star2-about-the-project)
  * [Screenshots](#camera-screenshots)
  * [Tech Stack](#space_invader-tech-stack)
  * [Architecture](#building_construction-architecture)
  * [Features](#dart-features)
  * [Issues](#fountain_pen-issues)
- [Roadmap](#compass-roadmap)
- [Contact](#handshake-contact)

<!-- About the Project -->
## :star2: About the Project

<!-- Web URL -->
### 🌐 Web URL
- http://27.96.134.7
<!-- Screenshots -->
### :camera: Screenshots
<div align="center"> 
  <img src="https://github.com/soonhankwon/dev-space-x-backend/assets/113872320/0a7a7f3b-b7d0-4e62-a6e0-bef64cf2149e" width="200" height="300" alt="screenshot"/>
  <img src="https://github.com/soonhankwon/dev-space-x-backend/assets/113872320/114c4e79-fef2-4b56-82a2-23bcfa90c9dd" width="200" height="300" alt="screenshot"/>
  <img src="https://github.com/soonhankwon/dev-space-x-backend/assets/113872320/d8e3e9b6-5181-4843-a1b7-a26bfdd9d2b4" width="200" height="300" alt="screenshot"/>
  <img src="https://github.com/soonhankwon/dev-space-x-backend/assets/113872320/5ec5879a-56cd-4c35-973f-39828bb95c16" width="200" height="300" alt="screenshot"/>
</div>

<!-- TechStack -->
### :space_invader: Tech Stack
<details>
  <summary>Client</summary>
  <ul>
    <li><a href="https://www.thymeleaf.org/">Thymeleaf</a></li>
    <li><a href="https://developer.mozilla.org/ko/docs/Web/JavaScript">Javascript</a></li>
  </ul>
</details>

<details>
  <summary>Server</summary>
  <ul>
    <li><a href="https://aws.amazon.com/ko/corretto/">Java 17 Amazon Corretto</a></li>
    <li><a href="https://spring.io/">SpringBoot 2.7.15</a></li>
    <li><a href="https://spring.io/projects/spring-data-jpa">Spring Data Jpa</a></li>
    <li><a href="https://spring.io/projects/spring-security">Spring Security 5.7.10</a></li>
    <li><a href="https://spring.io/guides/gs/messaging-stomp-websocket/">Spring WebSocket 5.3.29</a></li>
    <li><a href="https://docs.spring.io/spring-framework/reference/web/webflux.html">Spring WebFlux 5.3.29</a></li>
    <li><a href="https://github.com/TheoKanning/openai-java">theokanning.openai-gpt3-java 0.16.0</a></li>
  </ul>
</details>

<details>
<summary>Database</summary>
  <ul>
    <li><a href="https://www.mysql.com/">MySQL 8.0.33</a></li>
  </ul>
</details>

<details>
<summary>DevOps</summary>
  <ul>
    <li><a href="https://www.ncloud.com/product/database/cloudDbMysql">Naver Cloud DB for MySQL</a></li>
    <li><a href="https://www.ncloud.com/product/compute/server">Naver Cloud Compute Server</a></li>
    <li><a href="https://www.docker.com/">Docker</a></li>
    <li><a href="https://docs.github.com/ko/actions">Github Actions</a></li>
  </ul>
</details>

<!-- Architecture -->
### :building_construction: Architecture
<div align="center"> 
<img src="https://github.com/soonhankwon/tech-interview-defense/assets/113872320/dd084992-eeaf-4ce2-95a2-a9ebce0b10c0" width="540" height="330">
</div>

<!-- Features -->
### :dart: Features

- OpenAI API 를 사용한 AI 멘토링 및 기술 면접 디펜스 웹 애플리케이션입니다.
- 멘토링 시 주제별 전문가의 멘토링을 받을 수 있습니다.
- 질문에 대한 멘토링으로 진행되며, 프롬프트 엔지니어링으로 보다 정확하고 이해하기 쉽게 예시를 들어 해당 주제의 전문가 답변을 제공합니다.
- 멘토링 답변에 대한 탐구할만한 연관질문 생성기능(DEEP DIVE)을 제공합니다.
- 멘토링 모드는 StreamChatCompletion 기능이 구현되어 있습니다.
- 기술 면접 디펜스 모드는 AI 면접관의 질문을 잘 답변해서 스코어를 올리는 모드입니다.
- 디펜스 모드 클리어시 종합적인 피드백이 제공됩니다.

<!-- Issue -->
### :fountain_pen: Issues

- [기술 면접 디펜스 모드에서 AI의 상황에 맞지 않는 응답 다수 발생]
  * [프롬프트 엔지니어링의 지속적인 학습방법으로 개선](https://www.notion.so/AI-98e141417a1745a780ca57626429b144?pvs=4)
- [멘토링 대기 Latency 약 10~15초]
  * [GPT StreamChatCompletion 활용한 멘토링 응답대기 Latency 개선](https://www.notion.so/GPT-Stream-Completion-4dd198e0fe0745eab3ca1dad284c4e67?pvs=4)

<!-- Roadmap -->
## :compass: Roadmap

* [x] 멘토링모드 OpenAPI 스트리밍 기능 활용
* [x] 멘토링 답변에 대한 연관질문 생성 기능 구현 
* [ ] 디펜스모드 스트리밍 기능 활용 → 사용자 채팅 응답시간 편의성 개선 

<!-- Contact -->
## :handshake: Contact

Email - soonable@gmail.com
