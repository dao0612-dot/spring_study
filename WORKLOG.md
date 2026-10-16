# 작업 로그

일별 실제 작업 내용. 최근 날짜가 위.

---

## 2026-10-16

- 기능 정의서: 1주차 피드백 반영
  - 기술 스택 명시, 참고사항(롬복 VO와 record는 연습용으로 둘 다 사용)
  - 컬럼 순서 정리, 명명 규칙 통일(snake_case, `_idx`·`_yn`·`_dttm`, `reg_`·`upt_`·`del_`)
  - 등록·수정·삭제 컬럼 추가, 컬럼 주석, 제약 조건(`user_id` UNIQUE, 필수 컬럼 NOT NULL)
  - 글쓴이 컬럼을 회원번호(`member_idx`)로 FK 연결
  - 추천 기능은 추가 기능 후보로 분리
- DB: 정의서 SQL 기준으로 H2 `member`·`board` 테이블 재생성, 컬럼·제약 확인
- 코드 정리: 패키지 `VO` → `vo`, 메서드명(`list`, `boardPostSave`), `/hello2`를 `Map<Integer, String>`으로, Map 키 영문화, 미사용 필드(`hot`)·주석 코드 제거
- `ResponseEntity`: `/hello` 반환 타입을 `ResponseEntity<String>`으로 변경, Postman으로 200 / 201 / 400 / 헤더 추가 응답 확인
- 저장소: `.gitignore`로 Postman 작업 폴더 제외

## 2026-10-15

- 회원 VO: `MemberVo`(Lombok `@Getter`·`@Setter`·`@ToString(exclude = "password")`), 단위 테스트 `MemberVoTest`
- Controller
  - `@RestController`: `/hello`(문자열), `/hello2`(Map → JSON)
  - `@Controller`: `/` → `index.html`(Model → Thymeleaf `th:text`), `/hello3`(`@ResponseBody`)
- 게시글 (DB 연동 전 임시 데이터)
  - `/board`: 게시글 하나(`Map<String, Object>`)
  - `/api/board/list`: 게시글 목록(`List<Map<String, Object>>`) JSON
  - `/board/list` → `board_list.html`(`th:each` 표)
- 글쓰기: `GET /board/post` → `board_post.html`(form), `POST /board/post`(`@RequestParam`) → `redirect:/board/list`
- DB: `board` 테이블 생성(PK, FK)
- 저장소 생성 및 GitHub 업로드

## 2026-10-14

- 개발 환경 구축
  - H2 2.5.252 서버 모드, `board` DB 생성
  - Spring Boot 프로젝트 생성(Java 21, Spring Boot 3.5.16, Maven, MyBatis 3.0.5, Thymeleaf, Lombok), 기동 확인
- 기능 정의서 초안 작성(기능 목록, URL, 테이블)
- DB: `member` 테이블 생성
- `MyLearning`: 생성자·객체 연습
