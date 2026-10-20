# 작업 로그

일별 작업. 최근이 위. 주차별 정리는 `weekly/`에. 날짜 제목 앞의 `[N주차]`로 검색할 수 있다.

---

## [2주차] 2026-10-20(화)

**주요 작업**: 2주차 피드백 반영(이름 규칙·주석)

- 게시판 URL·메서드·Model 이름 통일: 글쓰기 `post` → `write`, 목록 `posts` → `boards`
- 메서드 이름 규칙: 화면 반환 = 화면 이름(`boardWrite`, `boardList`), 데이터 처리 = 동사 + 대상(`saveBoard`, `getBoardList`)
- 글쓰기 form의 작성자 입력칸 삭제(작성자는 회원 번호로 처리 예정)
- 컨트롤러·Service·record·Mapper XML 주석 추가, 미구현 저장 로직 TODO 표시

수정 파일: BoardController.java, BoardRestController.java, MemberController.java, MemberRestController.java, BoardService.java, BoardWriteRequest.java, BoardMapper.xml, board_list.html, board_write.html(← board_post.html), index.html

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
