# 작업 로그

일별 작업. 최근이 위. 주차별 정리는 `weekly/`에. 날짜 제목 앞의 `[N주차]`로 검색할 수 있다.

---

## [4주차] 2026-11-03(화)

**주요 작업**: 스프링 시큐리티 5단계(앞부분) — 본인 글만 수정·삭제

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 본인 글 검사(설계 5-5, 10장 11·16번 — 전날 정함): 실패 코드 `BOARD_NOT_OWNER`(403, "본인이 쓴 글만 수정·삭제할 수 있습니다.")와 새 예외 `NotOwnerException`, Service에 공통 검사(글을 **먼저 조회** → 없으면 "없음", 작성자 번호가 로그인 회원과 다르면 "본인 글 아님")를 두고 **수정 화면·수정 처리·삭제 처리** 세 곳이 사용(본인 글 검사를 제목 검사보다 먼저). 화면 컨트롤러는 남의 글이면 상세로 돌려보내며 안내, 없는 글은 지금처럼 목록 + 안내. 상세 화면에 안내 줄 추가 [Claude 단계 안내(파일·메서드·갈래별 이동 표) → 본인 작성 → Claude 검토(수정 화면만 목록으로 가던 것, 로그 구분자, 안내 줄 위치, 주석) → 수정]
- 확인: 회원 A의 글을 회원 B로 수정 화면(링크·주소 직접 입력)·수정·삭제 → 상세 + 안내, A로는 됨, 없는 번호는 목록 + 안내 [본인 확인]

수정 파일: common/exception/ErrorCode.java, common/exception/NotOwnerException.java(신규), BoardService.java, BoardController.java, board/detail.html

## [4주차] 2026-11-02(월)

**주요 작업**: 스프링 시큐리티 3·4단계 — 세션이 끊긴 뒤의 저장·가입·폼 요청 안내(CSRF 403), 글 작성자를 로그인한 회원으로, 연습용 코드 정리

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 지금 모습부터 확인: 글쓰기 화면을 열어 둔 채 다른 탭에서 로그아웃 → 저장 → 403 + 화면 안내 `undefined`. 설계에 적었던 예상(로그인 안 한 API 요청 → 401)과 다름 — 로그아웃이 세션과 그 안의 CSRF 원본 토큰을 같이 지우고, CSRF 검사가 로그인 확인보다 먼저라 거기서 막힘. 403 본문은 부트 기본 오류 JSON이라 `message` 칸이 없음. 세션 만료·서버 재시작도 같음. 지금 설정(폼 로그인)은 로그인 안 한 요청에 401이 아니라 로그인 화면 이동(302)을 주고, 비로그인이면 글쓰기 화면부터 열리지 않아 브라우저에선 401 경로가 사실상 없음 [Claude 추측·확인 방법 → 본인 실행]
- 처리 방향: 처음엔 `/api/**`에 시큐리티 필터 단계 처리기(403·401을 공통 응답 JSON으로)를 설계 → 의견 요청 → 받은 의견("보통 공통 오류를 쓰고, 401은 추측이 가능하니 else if로")에 따라 **화면 JS에서 상태 코드로 나눔**: 글쓰기는 401·403, 가입은 403에 고정 안내("화면이 만료되었습니다. 새로고침 후 다시 시도해주세요."), 그 밖은 서버 문구 `body.message ?? '오류가 발생했습니다.'`. 서버 처리기는 만들지 않음. 한 요청에서 403이 여러 종류로 올 수 있게 되면(수정 fetch: CSRF + 남의 글) `ErrorCode`에 줄을 더해 서버 문구 + `??` 기본 문구로(설계 5-3, 10장 14번) [Claude 설계 초안 → 의견 요청(본인 작성) → 본인 결정 → 본인 작성 → Claude 검토(들여쓰기)]
- 폼 POST(로그아웃·로그인·수정·삭제)의 CSRF 403은 `templates/error/403.html` 안내 화면 하나로(부트가 화면 요청 오류 때 `error/상태코드.html`을 찾음). 로그인 전에 그린 화면 + 다른 탭 로그인(토큰 교체) → 로그아웃 403을 재현해 원인 확인. 탭끼리 토큰을 맞추는 방법은 원본이 있을 때만 통하고 가장 흔한 만료·재시작은 못 막아 안내 화면을 기본으로(설계 10장 15번) [본인 발견·재현 → Claude 원인 설명·예시 → 본인 결정·적용 → 본인 확인]
- 작성자 연결(설계 5-4): 저장·수정·삭제의 임시 회원 번호 1 → 컨트롤러가 `@AuthenticationPrincipal LoginMember`로 로그인 회원을 받아 Service에 번호만 넘김(Service는 시큐리티를 모름). 화면 저장(예비 경로)·API 저장·수정·삭제 4곳 [Claude 예시(API 저장 한 곳) → 본인 나머지 작성 → Claude 검토(Service에 남은 시큐리티 import) → 수정 → 본인 확인(DB 작성자·수정자·삭제자 = 로그인 회원)]
- 확인: 글쓰기 열어 둠 + 다른 탭 로그아웃 → 저장 안내, 가입 화면 + 서버 재시작 → 가입 안내, 로그아웃 403 → 안내 화면, 제목 빈칸 → 기존 문구 그대로 [본인 확인]
- 연습용 메서드 지움(`/hello`·`/hello3`·`/board` Map 연습, 설계 10장 12번), 메인 화면의 안 쓰는 줄 정리 [Claude 일괄 → 본인 확인], 공통 메뉴에 글쓰기 링크 [본인]

수정 파일: BoardService.java, BoardController.java, BoardRestController.java, MemberController.java, MemberRestController.java, board/write.html, member/join.html, index.html, common/header.html, error/403.html(신규), 시큐리티 설계 문서(1·3·4·5-3장, 순서도, 7·8장, 10장 14·15번)

## [3주차] 2026-10-30(금)

**주요 작업**: 스프링 시큐리티 2단계 — DB 회원으로 로그인·로그아웃, 공통 메뉴

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 로그인 설계 세부를 시작 전에 정해 설계 문서 5-2에 추가: 로그인 화면·처리 주소 `/member/login`, 입력 칸 이름을 DB와 같게(`userId`·`pwd`), 로그인 화면은 form(처리하는 곳이 시큐리티 필터라 결과가 이동으로 옴), 성공 시 원래 가려던 곳 또는 목록, 실패 문구 하나(아이디 없음·비밀번호 틀림을 구분하지 않음), 로그아웃은 POST [Claude 설계 초안 → 본인 확인]
- `LoginMember`: 시큐리티 `User`를 상속하고 회원 번호를 더함(작성자 연결 때 씀) [Claude 예시 → 본인 분석·적용]
- `MemberDetailsService`(`UserDetailsService` 구현): 아이디로 회원을 찾아 `LoginMember`로 돌려줌, 없거나 사용 중지면 로그인 실패. 비밀번호 비교는 시큐리티가 BCrypt로 [Claude 예시 → 본인 분석·적용]
- `SecurityConfig`: 우리 로그인 화면·칸 이름·성공 이동·로그아웃 주소, 로그인 화면 공개 [Claude 예시 → 본인 적용 → Claude 검토(안 쓰는 import·주석) → 수정]
- 로그인 화면 `member/login.html`(form, 실패 문구), 공통 메뉴 조각 `common/header.html`(홈·게시판·로그인·회원가입·로그아웃 버튼)을 모든 화면 맨 위에, 메인 화면 정리, 가입 성공 후 우리 로그인 화면으로 [Claude 단계 안내(조각은 예시) → 본인 작성 → Claude 검토(링크 `th:href`) → 수정]
- 확인: 스프링 기본 계정이 사라짐, 가입한 회원으로 로그인, 틀린 비밀번호·없는 아이디는 같은 문구, 로그아웃 뒤 막힌 주소 → 로그인 → 원래 주소로, 로그인 뒤 글쓰기 저장 통과(폼의 CSRF 숨은 칸이 FormData로 감 → 헤더는 추가하지 않음, 설계 10장 7번) [본인 확인]

- 받은 의견 반영(예외 메시지는 개발자가 볼 메시지, 회원이 보는 메시지와 오류 코드는 따로): 실패 응답을 `ErrorCode` enum(상태 코드 + 회원 문구)으로 바꿈. 예외는 `ErrorCode`와 개발자용 메시지(로그, 어디서·무엇이·몇 번)를 따로 받고, API 처리기는 우리 예외 처리기 여러 개를 하나로 합쳐 `ErrorCode`의 상태·문구로 응답(4xx는 warn 한 줄, 5xx는 error + 스택을 로그에만), 화면 컨트롤러는 `ErrorCode` 문구를 화면에. 어제 만든 문구 상수 모음(`ErrorMessage`)은 지움. 확인: 가입 시험 5개, 화면 문구, 콘솔에 개발자용 문장, 디버거로 예외 생성부터 응답까지 따라감 [의견 → Claude 설계 반영·예시(enum·공통 예외) → 본인 작성 → Claude 일괄 교체(예외 5종·던지는 곳 14곳·처리기·화면 컨트롤러·시험)·컴파일 → 본인 확인]

수정 파일: config/SecurityConfig.java, member/security/LoginMember.java(신규), member/service/MemberDetailsService.java(신규), MemberController.java, common/header.html(신규), member/login.html(신규), index.html, board/*.html·member/join.html(공통 메뉴), 시큐리티 설계 문서(5-2·5-3·4장·10장 6~9번), common/exception/ErrorCode.java(신규)·CustomException.java·예외 5종·ApiExceptionHandler.java·ErrorMessage.java(삭제), BoardService.java, MemberService.java, BoardController.java, MemberJoinApiTest.java

## [3주차] 2026-10-29(목)

**주요 작업**: 스프링 시큐리티 1단계 — 설정 뼈대(주소 규칙·비밀번호 암호화 도구), 회원가입(화면 fetch + CSRF 헤더, 비밀번호 규칙, 아이디 중복 409, BCrypt 저장), 실패 안내 문구 한곳에 모음

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 시큐리티 의존성과 설정(`config/SecurityConfig`): 공개 주소(목록·상세·내려받기·가입·`/error` 등) 외에는 로그인 필요, 로그인은 기본 폼, 비밀번호 암호화 도구(BCrypt)를 빈으로 등록. 확인: 공개 주소는 로그인 없이, 글쓰기 주소는 로그인 화면으로, 잘못된 주소는 오류 화면(400), 토큰 없는 쓰기 요청은 로그인 여부와 상관없이 403(CSRF 검사가 로그인 확인보다 먼저) [Claude 예시 → 본인 분석·작성 → Claude 검토(상세 주소 `/**`, `/error`의 `/`) → 수정 → 본인 확인]
- 빈 등록(`@Component`·`@Bean`·`@Mapper`)과 생성자 주입, 인터페이스 타입으로 받기를 설정 파일·서비스 코드로 짚음. 암호화 도구 빈을 등록하자 스프링 기본 계정 로그인이 안 되는 것(기본 비교 도구가 우리 BCrypt로 바뀜)을 확인 [본인 질문 → Claude 설명 → 본인 정리]
- 회원가입: `JoinRequest`(로그에 비밀번호가 안 찍히게 `toString` 재정의), `MemberMapper`+XML(아이디로 조회·저장), `MemberService.join`(빈 칸 400 → 비밀번호 규칙 400 → 아이디 중복 409 → BCrypt 암호화 → 저장, 동시 가입으로 UNIQUE에 걸리면 409로 바꿔 던짐), `POST /api/member/join`(201, 응답에 입력값을 담지 않음), 새 예외 `DuplicateException`·`InvalidValueException`과 처리기 [Claude 단계 안내(처음 보는 정규식·`toString`은 예시) → 본인 작성 → Claude 검토(매퍼 `interface`·namespace, 정규식 괄호, 0줄 비교 방향, 잡은 예외를 다시 던지기) → 수정]
- 가입 화면 `member/join.html`: 글쓰기와 같은 fetch 방식 + 화면에 심은 CSRF 토큰을 읽어 요청 헤더에 담음, 실패 시 서버 문구 안내·입력 유지 [Claude 단계 안내(CSRF 부분은 예시) → 본인 작성 → Claude 검토(폼 `action` 제거) → 수정 → 본인 확인]
- 확인(설계 8장): 저장된 비밀번호가 BCrypt 암호문, 같은 비밀번호 두 회원의 암호문이 서로 다름, 중복 아이디 409, 규칙 위반·빈 칸 400, 실패 시 입력 유지 [본인 확인]
- 실패 안내 문구 정리: 게시판·회원이 같은 처리기를 쓰면서 처리기의 고정 문구가 안 맞게 됨 → 4xx 문구를 `common/exception/ErrorMessage` 상수 한곳에 모으고 던질 때 그 상수를 넣음, 처리기는 예외 메시지를 응답에. 5xx는 고정 문구 유지. 흩어져 있던 같은 뜻의 문장을 하나로(설계 문서 10장 5번, 확인 필요 표시) [본인 결정(문구를 한곳에 — 유지 보수, 회사 코드 구조 비교 후) → Claude 일괄 교체·컴파일 확인]

- 가입 API 첫 자동 시험(MockMvc) `MemberJoinApiTest`: 앱 전체를 켜고 가짜 요청을 필터부터 DB까지 흘려 정상 201·중복 409·규칙 위반 400·빈 칸 400·CSRF 없음 403과 응답 문구를 코드로 판정, 시험이 끝나면 DB를 되돌림(시험 회원이 남지 않음 확인). 일부러 기대를 깨 실패가 나는 것도 확인 [Claude 예시 → 본인 분석·실행 → 본인 확인]

수정 파일: pom.xml(시큐리티·시큐리티 시험 의존성), test/member/MemberJoinApiTest.java(신규), config/SecurityConfig.java(신규), member/dto/JoinRequest.java·member/mapper/MemberMapper.java·member/service/MemberService.java(신규), MemberMapper.xml(신규), member/join.html(신규), MemberController.java, MemberRestController.java, common/exception/DuplicateException.java·InvalidValueException.java·ErrorMessage.java(신규), ApiExceptionHandler.java, BoardService.java, BoardController.java, 시큐리티 설계 문서(10장 5번)

## [3주차] 2026-10-28(수)

**주요 작업**: 첨부 파일 내려받기(파일 업로드 완료), 구조 정리(도메인별 패키지·템플릿 폴더·루트 이름), 스프링 시큐리티 설계 문서

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 첨부 파일 내려받기: 파일 번호로 조회 → 원래 이름으로 저장되게 응답 헤더(`Content-Disposition`, 한글 이름 UTF-8)를 붙여 파일을 보냄, 상세의 파일 이름에 링크. 실패(없는 번호·삭제된 글·디스크에 없는 파일)는 404 — 설계의 "목록 + 안내"에서 바꿈(반환 타입이 파일 응답 하나라 화면 이동과 섞을 수 없음, 설계 문서 9장 8번). 응답 헤더·한글 이름·실패 경로 로그 확인 [Claude 예시 → 본인 분석·적용 → 본인 확인]
- 파일 업로드 흐름을 브레이크포인트로 따라가며 확인(서버 임시 파일 → 글 번호 채워짐 → 저장 이름·폴더 → 파일 정보 기록) [Claude 위치 안내 → 본인 확인]
- 구조 정리(3주차 피드백): 루트 패키지를 프로젝트 이름(`com.study.skillup`)으로, 도메인별 패키지(`board`·`member`·`common` 안에 controller·service·mapper·vo·dto), 파일·예외·공통 응답은 `common`으로, 템플릿을 `board/` 폴더로, 메인 클래스 `SkillUpApplication`. 회사 프로젝트는 계층별이지만 안 해 본 방식을 연습하려고 도메인별을 고름. 기존 기능·예외 처리 다시 확인 [본인 결정(구조·이름) → Claude 리팩토링(파일 이동·참조 수정·컴파일 확인) → 본인 확인]
- 스프링 시큐리티 설계 문서 `design/2026-10-28_시큐리티_회원가입_로그인.md`: 방식 비교(세션·JWT·HTTP Basic) 후 **세션 + 폼 로그인** 결정, 단계(가입 → 로그인 → 주소 막기 → 작성자 연결 → 본인 글만)와 날짜, 주소 규칙(API는 로그인 안 했으면 401 JSON), 순서도, 가입은 fetch("실패하면 그 자리에 머물게"), 비밀번호 8자 이상 + 영문·숫자 [Claude 설계 초안 → 본인 검토·결정(로그인 방식·가입 화면 방식·비밀번호 규칙)]

- 시큐리티 설계 검토 의견 반영: 비밀번호는 복호화가 안 되는 암호화라 `equals`가 아니라 전용 비교 함수(`matches`)로 — 설계 문서 5-2·확인 방법·10장 [의견 → Claude 정리 → 설계 반영]
- 내려받기 흐름을 디버거로 따라가며 확인(파일 번호 → 파일 정보 조회 → 디스크 파일 확인 → 응답 헤더), 내려받기 설계 문서의 실패 처리 설명을 실제 동작(404)과 맞춤 [Claude 위치 안내 → 본인 확인]
수정 파일: BoardController.java, board/detail.html, 설계 문서 2개(신규 1), 구조 정리로 Java 21개·테스트 3개·템플릿 5개 이동, 매퍼 XML 2개

## [3주차] 2026-10-27(화)

**주요 작업**: 작업 설계 문서(순서도 포함)를 먼저 만들어 확인받고 시작, 글쓰기 저장 fetch 전환(3주차 피드백), 상세 첨부 파일 목록, 내려받기 준비

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 3주차 피드백 확인: 파일 저장 후 DB 기록 실패 시 파일이 남지 않는 것 확인 [본인 확인]
- 하루 작업 설계 문서 `design/2026-10-27_글쓰기fetch_파일내려받기.md`: 개요·화면 흐름·처리 흐름·순서도(Mermaid)·API 명세·바꿀 파일·경우별 처리·확인 방법·정할 것. 작업 전에 보여 드리고 받은 의견 6건을 문서 9장에 "받은 의견 → 바꾼 것 → 이유"로 기록(첫 INSERT 실패는 롤백 없음, 상세 목록은 분기 없이 조회한 만큼, 내려받기 전송 중 실패, 예비 저장 경로 유지, 성공 후 이동 둘 다, 예상되는 오류는 직접 처리) [본인 결정(설계를 먼저 보여 드리기) → Claude 설계 초안·순서도 → 본인 검토·질문 → 의견 반영]
- 글쓰기 저장을 fetch로: 저장 API가 폼 데이터(multipart)를 받도록 변경, 글쓰기 화면은 제출 이벤트를 막고 FormData로 전송 → 성공 시 목록 이동, 실패 시 서버 문구·크기 초과·연결 실패 안내, 입력값·파일 유지, 전송 중 버튼 비활성화(중복 제출 방지) [피드백 → Claude 단계 안내(처음 보는 부분은 예시) → 본인 작성 → Claude 검토(`await` 누락, 주소 앞 `/`) → 수정 → 본인 확인(정상·빈 제목·큰 파일·연타)]
- 기존 form 저장 경로는 JS가 안 될 때의 예비 경로로 남기고 주석으로 표시 [의견 → 본인 설명·결정]
- API에 파일 저장 실패 전용 처리(500, 고정 문구, error 로그). 마지막 처리기는 예상치 못한 오류만 [의견 → Claude 위치 안내 → 본인 작성 → Claude 검토(로그에 예외 전달, 이름) → 수정]
- 상세에 첨부 파일 목록(원래 이름·크기), 글과 따로 조회(글 하나에 파일 여러 개) [Claude 단계 안내 → 본인 작성 → Claude 검토(`th:each` 위치, SQL 모양) → 수정]
- 내려받기 준비: 파일 번호로 조회하는 쿼리(글과 조인해 파일·글 모두 삭제 안 된 것만), 디스크 파일을 가리키는 `FileStore.load`, 원래 이름 + 파일을 묶는 `DownloadFile`, `BoardService.getBoardFile`(없으면 NotFound, 디스크에 없으면 error 로그) [쿼리·Service: Claude 단계 안내 → 본인 작성 → Claude 검토(`from`·칸 이름 오타, null 검사) → 수정 / `load`: Claude 예시 → 본인 분석·적용]. 컨트롤러와 링크는 다음에

수정 파일: BoardRestController.java, BoardController.java, BoardService.java, ApiExceptionHandler.java, BoardFileMapper.java·BoardFileMapper.xml, file/FileStore.java, file/DownloadFile.java(신규), board_write.html, board_detail.html, design/2026-10-27_글쓰기fetch_파일내려받기.md(신규)

## [3주차] 2026-10-26(월)

**주요 작업**: 2주차 피드백 마무리(화면 실패 문구), JS fetch 목록 화면, 파일 업로드(글쓰기)

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 화면 컨트롤러의 사용자 문구를 예외 메시지 대신 고정 문구로(7곳, 문구는 상수, 예외 메시지는 로그에만) [피드백 → Claude 위치 정리 → 본인 작성(상수 도입) → Claude 검토(`static`) → 수정]
- JS fetch 목록 화면: `/board/fetch`(화면만 반환) + JS가 `/api/board/list`를 받아 목록을 그림(제목은 글자로만 넣음) [Claude 단계 안내 → 본인 작성 중 막힘 → Claude 예시 → 본인 분석·적용]. fetch로 저장하기는 JS 공부와 함께 다음에
- 파일 업로드 설계: 게시글 전용 파일 테이블 `board_file`(글 번호 FK, 원래 이름·저장 이름·크기·삭제 칸), 여러 기능이 같이 쓰는 공용 테이블은 나중에 확장 [Claude 선택지·회사 구조 정리 → 본인 결정·설계 → Claude 검토 → 수정]
- 파일 업로드 구현: 디스크 저장 클래스 분리(UUID 저장 이름, 저장 폴더는 설정 파일), 글 저장과 파일 정보 기록을 한 트랜잭션으로(새 글 번호는 INSERT 후 받아 옴), 글쓰기 form에 파일 칸 [저장 클래스·Service: Claude 예시 → 본인 분석·적용 / VO·Mapper·form·컨트롤러: Claude 단계 안내 → 본인 작성 → Claude 검토 → 수정]
- 확인: 파일 있는/없는 글 저장, 저장 폴더에 UUID 이름 파일, `board_file` 행과 글 번호 대조. 크기 제한 초과는 컨트롤러 전에 413(오류 화면) → 안내 문구는 다음 작업 [Claude 제안 → 본인 확인]
- 3주차 피드백 반영: 파일을 디스크에 저장한 뒤 DB 기록이 실패하면 저장한 파일을 지우고 예외를 다시 던짐(글도 함께 롤백) [피드백 → Claude 방법 비교·삭제 메서드 예시 → 본인 try/catch 작성 → Claude 검토(삭제 경로) → 수정]. 일부러 실패시켜 보는 확인은 다음에

수정 파일: BoardController.java, BoardRestController.java, BoardService.java, BoardMapper.xml, board_write.html, application.properties, 기능 정의서 초안.txt, board_fetch.html(신규), file/FileStore.java·StoredFile.java(신규), vo/BoardFileVo.java(신규), mapper/BoardFileMapper.java·BoardFileMapper.xml(신규), 주말_시큐리티_예습(10.24~25)_임시.md(삭제)

## [2주차] 2026-10-23(금)

**주요 작업**: 게시글 삭제(CRUD 완료), 2주차 피드백 반영(SQL 작성 형식, 응답 객체 한 겹 + 공통 예외 처리기)

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 게시글 삭제: `POST /board/delete/{번호}`, 숨김 처리(`del_yn = 'Y'` + 삭제자·삭제 시각), 없는 글·이미 삭제된 글은 목록 + 안내, 상세에 삭제 버튼(POST form) [Claude 단계 안내 → 본인 작성 → Claude 검토(컴파일 오류 `;`) → 수정]
- SQL 작성 형식: 컬럼 한 줄에 하나, 콤마 앞, 조건의 `=` 붙임으로 쿼리 5개 통일 [피드백 → Claude 남은 곳 정리(INSERT는 회사 코드 모양 참고) → 본인 작성·세부 규칙 결정]
- 응답 객체 한 겹: 공통 예외 처리기 `ApiExceptionHandler`(`@RestControllerAdvice`, 예외 종류별 상태 코드 404·400·500)를 두고 JSON 컨트롤러의 try/catch·`ResponseEntity` 제거, 반환은 `ResponseDto`만(저장 201은 `@ResponseStatus`) [피드백 → Claude 사실 정리(회사 코드 구조 비교)·선택지 → 본인 방식 선택(처리기, 201 유지, 칸 유지) → Claude 단계 안내 → 본인 작성 → Claude 검토(로그 `{}` 누락, 예외 설명 정정) → 수정]
- 사용자에게 보이는 실패 문구를 예외 메시지 대신 처리기의 고정 문구로(예외 메시지는 로그에만), JSON 쪽 적용 [피드백(본인이 대화에서 다시 찾음) → Claude 선택지 → 본인 선택·작성]
- 처리기 확인: 정상 200·201, 빈 제목 400. 실험: 형식이 깨진 JSON이 마지막 `Exception` 처리기에 걸려 500으로 나옴(원래 400) [Claude 제안 → 본인 확인]
- 처리기 반환도 한 겹으로: 메서드마다 상태가 하나로 고정이라 `ResponseEntity` 대신 `@ResponseStatus` + `ResponseDto`, 깨진 JSON은 전용 처리(400, 사용자 문구는 할 수 있는 행동 안내, 로그는 `warn`) [본인 문제 제기(피드백 원문 재확인) → Claude 방법 정리 → 본인 작성 → Claude 검토(로그 레벨) → 수정]
- 주말 스프링 시큐리티 예습용 임시 문서(현재 상황·AI 사용 범위·예습 지도) [Claude 작성(본인 요청) → 본인 검토]
- 스프링 시큐리티 실습(`security-practice` 브랜치, main 미반영): 의존성 추가 시 동작 확인, 공개·로그인 주소 나누기(`SecurityFilterChain`), 기본 폼 로그인, CSRF 토큰 때문에 글쓰기 form을 `th:action`으로 [Claude 단계 안내 → 본인 작성·관찰(DEBUG 로그로 CSRF 403 확인) → Claude 설명]

수정 파일: BoardController.java, BoardRestController.java, BoardService.java, BoardMapper.xml, board_detail.html, exception/ApiExceptionHandler.java(신규), 주말_시큐리티_예습(10.24~25)_임시.md(신규, 10/26 삭제 예정)

## [2주차] 2026-10-22(목)

**주요 작업**: 2주차 피드백 반영 마무리(게시판 VO, 공통 응답, 공통 예외), 게시글 상세·수정

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- `MemberVo`·`BoardVo`를 테이블 칸에 맞춤(일시 칸은 `LocalDateTime`) [본인 작성 → Claude 검토(설계서와 칸 대조)]
- 게시판 객체(VO)로 전환: 저장은 Service에서 요청 DTO → `BoardVo`로 옮겨 Mapper에 전달, 목록·상세 조회도 `BoardVo`(MyBatis 밑줄 → camelCase 자동 변환 설정). 요청 DTO는 바깥 입력 거르기용으로 유지 [Claude 리팩토링 제안(피드백 지시) → 본인 방식 선택·작성 → Claude 검토 → 수정]
- 공통 응답: JSON 저장의 실패 응답·목록 API도 `ResponseDto`로 통일 [본인 작성 → Claude 검토(제네릭 타입 지정)]
- 공통 예외: `CustomException`(직접 던지는 예외의 공통 부모) 아래 `BlankValueException`·`DBNotAffectedException`·`NotFoundException`, 컨트롤러는 `CustomException` 하나로 받고 종류별 처리 [본인 "회사 코드 방식" 선택 → Claude 구조 정리·개념 설명 → 본인 작성 → Claude 검토 → 수정]
- 게시글 상세: `GET /board/detail/{번호}`, 없는 글이면 목록 + 안내(redirect + flash), 목록 제목에 상세 링크 [Claude 단계 안내 → 본인 작성 → Claude 검토 → 수정]
- 게시글 수정: 수정 화면(기존 값 채움)·처리(`POST /board/update/{번호}`), 수정자·수정 시각 기록, 없는 글이면 목록으로, 상세에 수정 시각 표시 [Claude 단계 안내 → 본인 작성 → Claude 검토(SQL `${}` → `#{}` 등) → 수정]
- 세션 번호가 URL에 붙지 않도록 설정(`server.servlet.session.tracking-modes=cookie`, 안내 문구 전달에 세션을 쓰면서 주소에 세션 번호가 노출되던 것) [Claude 원인 설명·설정 안내 → 본인 적용]
- 게시글 삭제(진행 중): 숨김 처리 SQL(`del_yn = 'Y'` + 삭제자·삭제 시각) 작성. Service는 빈 메서드, 컨트롤러·삭제 버튼 미작성 → 10/23 [본인 작성 → Claude 확인]

수정 파일: BoardController.java, BoardRestController.java, BoardService.java, BoardMapper.java, BoardMapper.xml, application.properties, board_list.html, MemberVo.java, vo/BoardVo.java(신규), exception/CustomException.java·BlankValueException.java·NotFoundException.java(신규), DBNotAffectedException.java, board_detail.html·board_edit.html(신규)

## [2주차] 2026-10-21(수)

**주요 작업**: 2주차 피드백 반영(URL·메서드 이름, 저장 결과 확인, 오류 로그, 공통 응답), 저장 데이터 구조 정리

작업 방식 표기: `[누가 먼저 → 다음 → …]`

- 저장 URL 변경: `POST /board/write` → `/board/save`, `/api/board/write` → `/api/board/save`. 이름 규칙 추가(데이터 처리 = `/대상/동사` ↔ `동사+대상`, 화면 = 화면 이름, `/api`로 화면·데이터 구분) [피드백 → Claude 사실 정리(회사 코드 URL 동사 집계)·선택지 2개 → 본인 선택·규칙 작성 → Claude 검토]
- 저장 데이터 구조: Service의 Map 옮겨 담기 제거, Mapper에 요청 record와 회원 번호를 따로 전달(`@Param`, 회원 번호는 요청으로 받지 않음) [본인 문제 제기 → Claude 실마리·DTO 개념 설명 → 본인 결정·작성 → Claude 검토]
- 저장 결과 확인: `insertBoard` 반환 행 수가 0 이하면 커스텀 예외 `DBNotAffectedException`, form은 글쓰기 화면 + 안내 / JSON은 500 + message [피드백 → Claude 개념 설명·예외 종류 선택지 → 본인 이름·구조 결정, 멀티 catch는 본인 검색 → 본인 작성 → Claude 검토]
- 오류 로그: 두 컨트롤러에 `@Slf4j`, 제목 없음 `warn`(메시지, 연습용) / 저장 실패 `error`(스택 포함) [Claude 개념 설명 → 본인 위치·대상·레벨 결정 → 본인 작성 → Claude 검토]
- 공통 응답 DTO `ResponseDto<T>(message, data)` 생성(상태는 HTTP 상태 코드, 모양은 DTO). JSON 저장의 성공 응답에 적용, 실패 응답(Map)은 다음 작업 [Claude 판단 기준 설명 → 본인 칸·이름·형식 결정 → 본인 작성]
- 피드백 질의응답: 커스텀 예외의 용도, `DataAccessException` vs `Exception` [Claude 설명 → 본인 정리해 답변]
- `MemberVo` 필드를 `member` 테이블에 맞춤(`memberIdx`·`userId`·`pwd`·`name`·`role`·`useYn`, 일시 칸은 다음) [본인 작업 → Claude 검토(`@ToString` 제외 대상 이름 불일치로 비밀번호 출력 → 수정)]

수정 파일: BoardController.java, BoardRestController.java, BoardService.java, BoardMapper.java, BoardMapper.xml, board_write.html, MemberVo.java, exception/DBNotAffectedException.java(신규), dto/ResponseDto.java(신규)

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
