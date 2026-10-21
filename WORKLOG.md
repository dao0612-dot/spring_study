# 작업 로그

일별 작업. 최근이 위. 주차별 정리는 `weekly/`에. 날짜 제목 앞의 `[N주차]`로 검색할 수 있다.

---

## [2주차] 2026-10-21(수)

**주요 작업**: 2주차 피드백 반영(URL·메서드 이름, 저장 결과 확인, 오류 로그, 공통 응답), 저장 데이터 구조 정리

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 저장 URL 변경: `POST /board/write` → `/board/save`, `/api/board/write` → `/api/board/save`. 이름 규칙 추가(데이터 처리 = `/대상/동사` ↔ `동사+대상`, 화면 = 화면 이름, `/api`로 화면·데이터 구분) [피드백 → Claude 사실 정리(회사 코드 URL 동사 집계)·선택지 2개 → 본인 선택·규칙 작성 → Claude 검토]
- 저장 데이터 구조: Service의 Map 옮겨 담기 제거, Mapper에 요청 record와 회원 번호를 따로 전달(`@Param`, 회원 번호는 요청으로 받지 않음) [본인 문제 제기 → Claude 실마리·DTO 개념 설명 → 본인 결정·작성 → Claude 검토]
- 저장 결과 확인: `insertBoard` 반환 행 수가 0 이하면 커스텀 예외 `DBNotAffectedException`, form은 글쓰기 화면 + 안내 / JSON은 500 + message [피드백 → Claude 개념 설명·예외 종류 선택지 → 본인 이름·구조 결정, 멀티 catch는 본인 검색 → 본인 작성 → Claude 검토]
- 오류 로그: 두 컨트롤러에 `@Slf4j`, 제목 없음 `warn`(메시지, 연습용) / 저장 실패 `error`(스택 포함) [Claude 개념 설명 → 본인 위치·대상·레벨 결정 → 본인 작성 → Claude 검토]
- 공통 응답 DTO `ResponseDto<T>(message, data)` 생성(상태는 HTTP 상태 코드, 모양은 DTO). JSON 저장의 성공 응답에 적용, 실패 응답(Map)은 다음 작업 [Claude 판단 기준 설명 → 본인 칸·이름·형식 결정 → 본인 작성]
- 피드백 질의응답: 커스텀 예외의 용도, `DataAccessException` vs `Exception` [Claude 설명 → 본인 정리해 답변]

수정 파일: BoardController.java, BoardRestController.java, BoardService.java, BoardMapper.java, BoardMapper.xml, board_write.html, exception/DBNotAffectedException.java(신규), dto/ResponseDto.java(신규)

## [2주차] 2026-10-20(화)

**주요 작업**: 2주차 피드백 반영(이름 규칙·주석), 게시글 저장(INSERT)

- 게시판 URL·메서드·Model 이름 통일: 글쓰기 `post` → `write`, 목록 `posts` → `boards`
- 메서드 이름 규칙: 화면 반환 = 화면 이름(`boardWrite`, `boardList`), 데이터 처리 = 동사 + 대상(`saveBoard`, `getBoardList`)
- 글쓰기 form의 작성자 입력칸 삭제(작성자는 회원 번호로 처리 예정)
- 주석 추가(게시판 코드 전체, 회원 쪽은 연습용 표시), 미구현 부분 TODO 표시(작성자 회원 번호, MemberVo 필드)
- 게시글 저장: form·JSON 두 입구 → `BoardService.saveBoard` → `BoardMapper.insertBoard`(작성자 회원 번호는 로그인 전까지 임시 고정)
- 제목 검증을 Service로 이동(예외), 입구별 처리: JSON 400 + message / form은 글쓰기 화면에 안내 문구

수정 파일: BoardController.java, BoardRestController.java, MemberController.java, MemberRestController.java, BoardService.java, BoardMapper.java, BoardWriteRequest.java, MemberVo.java, BoardMapper.xml, board_list.html, board_write.html(← board_post.html), index.html

## [2주차] 2026-10-19(월)

**주요 작업**: JSON 받기(`@RequestBody`), Controller 마무리, Service·Mapper로 DB 목록 조회

- `POST /api/board/post`: JSON 받기(Map → record), 응답 201·400(`ResponseEntity`), Postman 확인
- 요청 DTO `BoardWriteRequest`(record)·단위 테스트
- 글쓰기 form을 `@ModelAttribute` + record로 변경, form·JSON 요청 비교(Content-Type·Status)
- `BoardService`(생성자 주입)로 목록 로직 분리
- MyBatis 연동: `BoardMapper`(인터페이스 + XML), 게시글 목록을 DB에서 조회(H2 대문자 컬럼명 → 별칭으로 해결)

수정 파일: BoardRestController.java, BoardController.java, dto/BoardWriteRequest.java, BoardWriteRequestTest.java, service/BoardService.java, mapper/BoardMapper.java, mapper/BoardMapper.xml, application.properties

## [1주차] 2026-10-16(금)

**주요 작업**: 1주차 피드백 반영, ResponseEntity·Postman 실습

- 기능 정의서: 명명 규칙 통일, 등록·수정·삭제 컬럼 추가, 주석·제약 조건, FK 적용 범위 결정
- DB: `member`·`board` 테이블 재생성
- 코드 이름 정리(패키지·메서드·Map 키)
- `/hello`를 `ResponseEntity`로 변경, Postman으로 응답 확인

수정 파일: 기능 정의서 초안.txt, MemberVo.java, MemberVoTest.java, MemberController.java, MemberRestController.java, BoardController.java, BoardRestController.java, board_list.html, .gitignore

## [1주차] 2026-10-15(목)

**주요 작업**: Controller 구현(화면·JSON), 글쓰기 form

- 회원 VO(Lombok)·단위 테스트
- Controller: 화면(Thymeleaf), JSON(`@RestController`)
- 게시글 목록·상세 임시 데이터(Map·List)
- 글쓰기 form → `@RequestParam` → redirect
- `board` 테이블 생성, GitHub 업로드

수정 파일: MemberVo.java, MemberVoTest.java, MemberController.java, MemberRestController.java, BoardController.java, BoardRestController.java, index.html, board_list.html, board_post.html, 기능 정의서 초안.txt

## [1주차] 2026-10-14(수)

**주요 작업**: 개발 환경 구축, 기능 정의서 초안

- H2 서버 모드 DB 생성
- Spring Boot 3.5.16 / Java 21 프로젝트 생성, 기동 확인
- 기능 정의서 초안, `member` 테이블 생성

수정 파일: pom.xml, application.properties, 기능 정의서 초안.txt, MyLearning(Main.java, Member.java)
