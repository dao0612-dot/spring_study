# 작업 로그

일별 작업. 최근이 위. 주차별 정리는 `weekly/`에. 날짜 제목 앞의 `[N주차]`로 검색할 수 있다.

---

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
