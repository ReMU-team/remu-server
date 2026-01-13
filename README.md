# remu-server


여행 감정 기반 기록 서비스 REMU의 백엔드 서버입니다.

## 🛠 Tech Stack


- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **Database**: MySQL, Redis
- **ORM**: Spring Data JPA
- **Infrastructure**: AWS (EC2, S3, RDS)

## 🌿Branch Strategy


### 📌 Branch Types
| Branch | Description |
|------|-----------|
| `main` | 운영/배포 브랜치 |
| `develop` | 개발 통합 브랜치 |
| `feature/{이슈번호}-{기능명}` | 기능 개발 브랜치 |

<br>

### 💡 Branch Naming Convention
- **형식**: `{type}/{issue-number}-{description}` (전체 소문자, 공백은 `-`로 연결)
- **예시**:
  - `feature/12-user-login`
  - `fix/45-auth-header-error`

```bash
git checkout develop
git pull origin develop
# 이슈 12번인 로그인 기능을 개발할 경우
git checkout -b feature/12-user-login
```

<br>

### 🚦Branch Rules

- `main`
    - 직접 커밋 ❌
    - 배포 시점에만 `develop`에서 merge

- `develop`
    - 직접 커밋 ❌
    - 모든 기능 PR의 대상 브랜치

- `feature/*`
    - `develop` 기준으로 생성
    - 기능 단위 작업
    - 작업 완료 후 PR → `develop`

<br>

## 📝 Commit Convention



커밋 메시지는 아래 컨벤션을 따릅니다.

### 📌 Types

| Type | Description |
|-----|------------|
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 (기능 변경 없음) |
| `chore` | 설정, 빌드, 기타 작업 |
| `docs` | 문서 수정 |
| `test` | 테스트 코드 추가/수정 |

<br>

## 🔄 Development Flow



본 프로젝트는 **이슈 기반 개발 흐름**을 따릅니다.

```text
Issue 생성
 → develop 기준 feature 브랜치 생성
 → 기능 개발 및 커밋
 → Pull Request 생성 (to develop)
 → Code Review
 → Merge
```
<br>

## 📂 Project Structure (Domain-based)


본 프로젝트는 도메인 중심 패키지 구조를 사용합니다.
```
src/main/java/com/remu-server
├── domain
│   ├── user            # 유저 도메인 (A)
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   └── entity
│   ├── galaxy          # 은하/여행 도메인 (B)
│   ├── resolution_card # 다짐 도메인 (C)
│   ├── star            # 기록/별 도메인 (D)
│   ├── review_card     # 회고 도메인 (C)
│   └── alarm           # 알림 도메인 (B)
├── global              # 공통 설정 및 유틸리티
│   ├── config          # Security, Swagger 등 설정
│   ├── entity          # 공통 BaseEntity
│   ├── apiPayload      # 예외 처리 (Exception Handler)
│   └── code            # 공통 에러코드

```
