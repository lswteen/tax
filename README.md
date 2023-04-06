# 프로젝트 이름: Jobis Tax Application

## 소개

Jobis Tax Application은 사용자 인증 및 회원 정보 스크랩을 위한 RESTful API를 제공하는 웹 애플리케이션입니다. 이 애플리케이션은 Spring Boot, Spring Security, Hibernate Validator, Spring Cloud OpenFeign 등의 기술을 사용하여 구축되었습니다. 회원 관련 기능, 인증 관련 기능 등의 엔드포인트를 제공합니다.

## 기능

1. **회원 기능**: 회원 정보 조회 및 정보 스크랩 기능을 제공합니다. 회원 정보는 사용자 인증 후에 조회할 수 있습니다.
2. **인증 기능**: 로그인, 토큰 재발행, 회원 가입, 로그아웃 기능을 제공합니다. 이러한 기능들은 JWT(Json Web Token)를 사용하여 인증합니다.

## 엔드포인트

### User Controller

- `GET /szs/me`: 로그인한 회원의 정보를 조회합니다.
- `POST /szs/scrap`: 회원의 토큰 정보를 스크랩합니다.

### Auth Controller

- `POST /szs/login`: 사용자 로그인을 처리하고 JWT 토큰을 발급합니다.
- `POST /szs/reissuance`: 기존 토큰을 이용해 새로운 토큰을 발급합니다.
- `POST /szs/signup`: 새로운 회원을 등록합니다.
- `DELETE /szs/signout`: 로그아웃 기능을 제공합니다.

## 프로젝트 구조

이 프로젝트는 주요 패키지로 `com.jobis.tax.application.controller`, `com.jobis.tax.application.request`, `com.jobis.tax.application.response`, `com.jobis.tax.application.security.dto`, `com.jobis.tax.application.service` 등이 있습니다.

### 주요 기술 스택

- Spring Boot
- Spring Security
- Hibernate Validator
- Spring Cloud OpenFeign
- Lombok
- H2 Database
- SpringDoc OpenAPI UI

## 설정

이 프로젝트의 주요 설정은 `application.yml` 파일에서 확인할 수 있습니다. 이 설정 파일에서는 다음과 같은 내용을 설정할 수 있습니다.

- OpenAPI 설정 파일
- JPA 설정
- H2 데이터베이스 설정
- 데이터 소스 설정
- SpringDoc 설정
- JWT 설정
- 암호화 키 및 벡터 설정
- 서버 설정

## 빌드 및 실행 방법

프로젝트는 Gradle을 사용하여 빌드할 수 있습니다.

1. 프로젝트 디렉터리로 이동합니다.
2. 다음 명령어를 실행하여 프로젝트를 빌드합니다.
3. 빌드가 완료되면 `build/libs` 디렉터리에서 실행 가능한 JAR 파일을 찾을 수 있습니다. JAR 파일을 실행하여

