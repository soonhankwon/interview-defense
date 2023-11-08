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
  * [api 구현과정](#rocket-api-구현과정)
  * [이슈 해결과정](#checkered_flag-이슈-해결과정)
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
- ws://43.202.192.55/v1/chat
  - 채팅메세지를 받아 멘토링을 제공하는 웹소켓 엔드포인트 API 
<!-- ERD -->
### :book: erd

<details>
<summary>Diagram - click</summary>
<div markdown="1">
 
![tech-mentor-erd](https://github.com/soonhankwon/tech-mentor-backend/assets/113872320/c7537e0b-1777-4208-9b54-2bd3f65dfac0)
 
</div>
</details>

<!-- api 구현 과정 -->
### :rocket: api 구현과정

<!-- 이슈 해결 과정 -->
### :checkered_flag: 이슈 해결과정
<details>
<summary>AI의 상황에 맞지 않는 응답 다수 발생: AI의 지속적인 학습방법으로 개선 - click</summary>
<div markdown="1">

```plain
- AI가 채팅흐름에서 자신의 답변이 무엇이었는지 망각하는 경우가 40~50%의 높은 확률로 발생하였습니다.
- 어떻게하면 AI가 채팅흐름을 잘 기억하게 할 수 있을까?라는 고민을 했습니다.
- 프롬프트 엔지니어링 방법중 AI의 지속적인 학습에 관한 레퍼런스를 참조했습니다.

- AI의 이전 답변을 프롬프트에 추가시켜주는 방법을 적용했습니다.
  * ex) ${이전 AI답변} 에 대한 탐구할 수 있는 질문 목록을 추천해주세요.
- 해당 방법 적용으로 딥다이브 기능의 경우 현재까지 망각하는 케이스는 검출되지 않았습니다.
```
</div>
</details>

<details>
<summary>사용자의 질문에 대한 멘토링 대기 Latency 약 17~18초 발생: GPT StreamChatCompletion & WebSocket을 활용한 개선 - click</summary>
<div markdown="1">

```plain
- 멘토링 모드의 기존 GPT ChatCompletion 사용시 약 17~18초 정도의 Latency 를 보였습니다.
- 더 심각한 문제는 일반적인 HTTP통신 특성상 해당 시간동안 사용자는 아무것도 보지 못하고 대기하고 있다는 점입니다. 

- 해당 대기시간으로 사용자에게 지루함을 느끼게하고, 서비스를 이탈할 것이라는 문제점을 파악했습니다.

- 실제 ChatGPT처럼 실시간 스트림으로 서비스를 제공할수 있을까?라는 고민을 했습니다.
  * OpenAPI 레퍼런스를 살펴보니 ChatCompletion 이외에 StreamChatCompletion 서비스를 제공하고 있었습니다.
  * StreamChatCompletion 서비스란 기존에 "JAVA란 객체지향.."라는 응답을 "J", "AVA", "란 객체", "지향.", ".","null"의 chunk로 실시간으로 조각조각 응답해주는 서비스입니다.

- 실시간으로 수십 ~ 수백 ~ 수천개를 응답받는 특성상 웹소켓 프로토콜이 적합하다고 생각했습니다.
  * 연결을 한 번 맺어놓고 응답을 쭈~~욱 받아서 채팅방에 실시간으로 렌더링해준다!

- 아래는 웹소켓과 StreamChatCompletion을 구현한 코드입니다.
- 스트림 서비스 사용으로 사용자는 실시간으로 응답을 볼 수 있게 되었습니다. 전체적인 응답대기 시간은 약 1~2초 개선되었습니다.
```
```java
private void subscribeFlowable(WebSocketSession session, Chat chat, StringBuilder sb, Flowable<ChatCompletionChunk> responseFlowable) {
        StringBuilder chunkBuffer = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        // StreamChatCompletion OpenAPI 서비스를 subscribe하는 로직입닌다.
        responseFlowable.subscribe(
                chunk -> {
                    try {
                        String response = chunk.getChoices().get(0).getMessage().getContent();
                        // 해당 서비스의 마지막 응답에는 항상 null이 들어옵니다. 이것을 FinishFlag로 사용합니다.
                        if (!hasStreamFinishFlag(response)) {
                            chunkBuffer.append(response);
                            sb.append(response);
                            // 버퍼에 chunk를 저장해놓고 5개가 되면 소켓에 전송합니다. 
                            if (chunkBuffer.length() >= 5) {
                                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chunkBuffer.toString())));
                                chunkBuffer.setLength(0);
                            }
                            return;
                        }
                        if (hasBufferRemainingChunk(chunkBuffer)) {
                            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chunkBuffer.toString())));
                            chunkBuffer.setLength(0);
                        }
                        applicationEventPublisher.publishEvent(new MessageSendEvent(new ChatMessage(sb.toString(), chat, ChatSender.AI)));
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(null)));
                        sb.setLength(0);

                        long end = System.currentTimeMillis();
                        log.info("streaming spend time={}", end - start);
                    } catch (Exception e) {
                        log.error("An error occurred while processing the flowable", e);
                    }
                },
                Throwable::printStackTrace
        );
    }
```
</div>
</details>

</div>
</details>

<details>
<summary>GPT StreamChatCompletion 응답시간 개선: 버퍼를 활용하여 약1.2초, 개선율 6.87% - click</summary>
<div markdown="1">

```plain
- GPT StreamChatCompletion은 활용한 멘토링 기능은 서비스의 핵심 기능입니다.
- 어떻게 하면 조금더 응답시간을 개선시킬수 있을까?라는 고민을 하였습니다.

- 메모리와 하드디스크간에 속도차이 때문에 버퍼가 있는것처럼 여기에도 적용시킨다면 개선이되지 않을까?라는 생각이 들었습니다.
- 아래의 코드처럼 5개씩 버퍼에 모아서 웹소켓에 전달해주는 방식을 적용했습니다.
- 간단한 테스트 케이스들을 통해 Latency 감소 약1.2초, 개선율은 6.87%을 보였습니다.(기존 약18초 -> 약 16초 후반, 17초)
```
```java
private void subscribeFlowable(WebSocketSession session, Chat chat, StringBuilder sb, Flowable<ChatCompletionChunk> responseFlowable) {
        // 커스텀하게 만든 chunk(OpenAPI에서 응답받는 조각데이터) 버퍼입니다.
        StringBuilder chunkBuffer = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        // StreamChatCompletion OpenAPI 서비스를 subscribe하는 로직입닌다.
        responseFlowable.subscribe(
                chunk -> {
                    try {
                        String response = chunk.getChoices().get(0).getMessage().getContent();
                        // 해당 서비스의 마지막 응답에는 항상 null이 들어옵니다. 이것을 FinishFlag로 사용합니다.
                        if (!hasStreamFinishFlag(response)) {
                            // 버퍼에 chunk 데이터를 넣어줍니다.
                            chunkBuffer.append(response);
                            sb.append(response);
                            // 버퍼에 chunk를 저장해놓고 5개 이상이라면 소켓에 전송합니다. 
                            if (chunkBuffer.length() >= 5) {
                                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chunkBuffer.toString())));
                                // 버퍼를 비워줍니다.
                                chunkBuffer.setLength(0);
                            }
                            return;
                        }
                        // 응답이 끝났는데 버퍼에 chunk가 남아있다면 소켓에 전송합니다.
                        if (hasBufferRemainingChunk(chunkBuffer)) {
                            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chunkBuffer.toString())));
                            chunkBuffer.setLength(0);
                        }
                        applicationEventPublisher.publishEvent(new MessageSendEvent(new ChatMessage(sb.toString(), chat, ChatSender.AI)));
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(null)));
                        sb.setLength(0);

                        long end = System.currentTimeMillis();
                        log.info("streaming spend time={}", end - start);
                    } catch (Exception e) {
                        log.error("An error occurred while processing the flowable", e);
                    }
                },
                Throwable::printStackTrace
        );
    }
```
</div>
</details>

<details>
<summary>멘토링 서비스시 채팅(Chat)조회 DB 콜 개선 - click</summary>
<div markdown="1">

```plain
- StreamCompletionHandler에서 채팅 메세지를 저장할 때 Chat객체를 조회하는 DB콜을 어떻게하면 줄일수 있지않을까?라는 생각이 들었습니다. 
- 프론트에서 백엔드 요청에 chatId + 메세지 종류 플래그 + 메세지를 전송하면 이것을 split 으로 분리 그리고 Chat(사용자의 채팅방)을 chatId로 조회해서 ChatMessage DB에 메세지를 저장하는 로직이었습니다.

- 자체적으로 메모리에 웹소켓세션ID를 Key로하고 Value를 Chat으로 캐싱하여 사용하면 DB콜을 줄이고 성능을 개선시킬수 있을것이라고 예상했습니다.
  * ConcurrentHashMap을 사용하는 메모리 저장소 컴포넌트를 CacheStore라고 명하여 만들었습니다.
  * 해당부분은 Redis와 같은 In-memory DB로 대체할 수 있는 부분이지만, 싱글 인스턴스인 현재 애플리케이션 구조상 ConcurrentHashMap으로 충분하다고 생각했습니다.
- 아래는 Before와 After의 코드입니다.
- 테스트 결과 중복되는 DB콜을 줄일수 있었습니다. Latency 감소는 약 4.6초(기존 약 17초 -> 약 12.5초), 개선율은 26.93%을 보였습니다.
```
- Before
```java
@Override
public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		if(payload.contains(DEEP_QUESTION_FLAG)) {
		  // 시그널1! split으로 코드가독성이 떨어지고 형변환 코드 또한 지속적으로 생김
    String[] payloadSegments = payload.split(DEEP_QUESTION_FLAG);
    Long chatId = Long.parseLong(payloadSegments[0]);
    String userMessage = payloadSegments[1];
		
		  // 시그널2! 지속해서 chat을 DB에서 조회하는데 불필요하지 않을까?
    Chat chat = chatRepository.findById(chatId)
           .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_CHATROOM_IN_DB));
		  ChatMessage chatMessageDesc = chatMessageRepository.findTopByChatOrderByCreatedAtDesc(chat)
           .orElseThrow(() -> new ApiException(CustomErrorCode.NOT_EXISTS_LATEST_CHAT_MESSAGE));

    chatMessageRepository.save(new ChatMessage(userMessage.substring(3), chat, ChatSender.USER));
    Flowable<ChatCompletionChunk> responseFlowable = chatServiceV2.generateStreamResponse(chat, "[" + chatMessageDesc.getMessage() +"]" + "글에서" + userMessage);
		.........
		//비슷한 로직들 ...........
}
```
- After
```java
// 커스텀한 메모리 캐싱 스토어입니다.
@Component
public class ChatCacheStore {

    // Key: WebSocketSessionId, Value: Chat
    private final Map<String, Chat> webSocketSessionUserChatMap;

    public ChatCacheStore() {
        this.webSocketSessionUserChatMap = new ConcurrentHashMap<>();
    }

    public void cacheChatSessionIdAndChat(String chatSessionId, Chat chat) {
        this.webSocketSessionUserChatMap.put(chatSessionId, chat);
    }

    // key가 캐싱되어있다면 스토어에서 chat을 가져옵니다.
    public <T> Chat getChatByCacheKey(T key) {
        if(key instanceof String) {
            log.info("cache hit={}", key);
            return webSocketSessionUserChatMap.get(key);
        }
        throw new IllegalArgumentException("invalid key!!");
    }

    // key가 캐싱되어있다면 스토어에서 chat을 삭제합니다(리소스 정리).
    public <T> void removeCache(T key) {
        if(key instanceof String) {
            this.webSocketSessionUserChatMap.remove(key);
        }
        throw new IllegalArgumentException("invalid key!!");
    }
```
```java
    // 웹소켓 세션이 끝난다면 캐시스토어에서 리소스정리를 합니다.
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String chatSessionId = session.getId();
        Objects.requireNonNull(chatSessionId);
        cacheStore.removeCache(chatSessionId);
        session.close();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        start = System.currentTimeMillis();
        String payload = message.getPayload();
        String chatSessionId = session.getId();
        // 프론트에서 웹소켓 시작 Flag를 받는다면 캐시스토어에 웹소켓세션ID와 Chat(payload의 정보로 객체 생성)을 저장합니다.
        if(hasStartFlag(payload)) {
            saveChatInMap(payload, chatSessionId);
            return;
        }
        StringBuilder sb = new StringBuilder();
        // 이후 캐시스토어에 웹소켓세션ID가 있다면 캐시히트되어 DB조회없이 chat을 사용합니다.
        Chat chat = cacheStore.getChatByCacheKey(chatSessionId);
        if (hasDeepFlag(payload)) {
            applicationEventPublisher.publishEvent(new MessageSendEvent(new ChatMessage(DEEP_DIVE, chat, ChatSender.USER)));
            Flowable<ChatCompletionChunk> responseFlowable =
                    openAiChatService.generateStreamResponse(chat, "[" + payload.replace(DEEP_QUESTION_FLAG, "").trim() + "]" + PromptGenerator.DEEP_DIVE);
            subscribeFlowable(session, chat, sb, responseFlowable);
        } else {
            applicationEventPublisher.publishEvent(new MessageSendEvent(new ChatMessage(payload, chat, ChatSender.USER)));
            Flowable<ChatCompletionChunk> responseFlowable = openAiChatService.generateStreamResponse(chat, payload);
            subscribeFlowable(session, chat, sb, responseFlowable);
        }
    }
```
</div>
</details>

- 문서화 중인 이슈 해결 리스트들
  - [NCloud 크레딧 소진으로 인한 AWS 이전 - 클라우드 인프라 구축]
  - [RestAPI로 리팩토링 - Thymeleaf 제거 및 프론트엔드 React를 활용한 VIEW로직을 백엔드에서 분리] 

<!-- 로드맵 -->
## :compass: 로드맵

* [x] 멘토링모드 OpenAPI 스트리밍 기능 활용
* [x] 멘토링 답변에 대한 연관질문 생성 기능 구현
* [x] NCloud 에서 AWS로 서버이전
* [x] Backend RestAPI로 리팩토링 + 프론트 React로 렌더링 작업

<!-- Contact -->
## :handshake: Contact

Email - soonable@gmail.com
