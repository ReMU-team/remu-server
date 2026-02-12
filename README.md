# remu-server


여행 감정 기반 기록 서비스 REMU의 백엔드 서버입니다.

<br>

## 👥 Team

| <a href="https://github.com/hyunjun2001"><img src="https://github.com/hyunjun2001.png" width="80"/></a> | <a href="https://github.com/hesseo"><img src="https://github.com/hesseo.png" width="80"/></a> | <a href="https://github.com/woong-ja"><img src="https://github.com/woong-ja.png" width="80"/></a> | <a href="https://github.com/Hanharam"><img src="https://github.com/Hanharam.png" width="80"/></a> |
|:--:|:--:|:--:|:--:|
| [매튜 / 진현준](https://github.com/hyunjun2001) | [요시 / 김희서](https://github.com/hesseo) | [웅표빠잉 / 이웅재](https://github.com/woong-ja) | [요시 / 한하람](https://github.com/Hanharam) |
| Backend Developer | Backend Developer | Backend Developer | Backend Developer |

<br>

## 🛠 Tech Stack


- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Infrastructure**: AWS (EC2, S3, RDS)

<br>

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
│   ├── user            # 유저 도메인 
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── dto
│   │   ├── exception
│   │   └── entity
│   ├── galaxy          # 은하/여행 도메인 
│   ├── resolution_card # 다짐 도메인 
│   ├── star            # 기록/별 도메인
│   ├── review_card     # 회고 도메인
│   ├── feedback        # 피드백 도메인
│   ├── place           # 장소 도메인 
│   └── alarm           # 알림 도메인 
├── global              # 공통 설정 및 유틸리티
│   ├── apiPayload      # 예외 처리 (Exception Handler)
│   ├── auth            # 인증/인가 관련 로직 (JWT, OAuth, 토큰 필터 등)
│   ├── common          # 공통 유틸 클래스
│   ├── config          # Security, Swagger 등 설정
│   ├── entity          # 공통 BaseEntity
│   ├── fcm             # Firebase Cloud Messaging 설정 및 푸시 알림 전송
│   ├── apiPayload      # 예외 처리 (Exception Handler)
│   └── s3              # AWS S3 파일 업로드/다운로드 관리

```
