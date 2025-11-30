# Boulderside

## 📖 소개 (Overview)

- Boulderside 백엔드 API를 관리하는 레포지토리입니다.  

## 🛠 기술 스택 (Tech Stack)

| 구분       | 기술 / 라이브러리              |
|----------|-----------------------------|
| 언어       | Java 21                      |
| 프레임워크  | Spring Boot 3.4.7       |

## 📁 패키지 구조 (Package Structure)
- `domain/feature/<name>`: 기능(바위, 루트 등)별 엔티티·서비스·리포지토리. 각 기능 안에 `interaction`이나 `external` 서브 패키지가 붙어 맥락을 설명합니다.
- `domain/feature/<name>/external`: 해당 도메인에서 사용하는 외부 API 어댑터와 DTO/매퍼 (예: `weather/external/*`).
- `infrastructure/aws/s3`, `infrastructure/cache/redis`: AWS S3, Redis 등 기술 의존성 어댑터. 비즈니스 규칙 없이 순수 기술 호출만 담당합니다.

## 📜 변경 내역 (Changelog)
최신 변경 내역은 [CHANGELOG.md](./CHANGELOG.md)에서 확인하세요.
