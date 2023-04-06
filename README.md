# 프로젝트 이름: Jobis Tax Application

## 소개

Jobis Tax Application은 사용자 인증 및 회원 정보 스크랩을 위한 RESTful API를 제공하는 웹 애플리케이션입니다.
이 애플리케이션은 Spring Boot, Spring Security, Hibernate Validator, HttpClient 등의 기술을 사용하여 구축 되었습니다.
회원 관련 기능, 인증 관련 기능 등의 엔드포인트를 제공합니다.

## 기능

1. **회원 기능**: 회원 정보 조회 및 정보 스크랩 기능을 제공합니다. 회원 정보는 사용자 인증 후에 조회할 수 있습니다.
2. **인증 기능**: 로그인, 토큰 재발행, 회원 가입, 로그아웃 기능을 제공합니다. 이러한 기능들은 JWT(Json Web Token)를 사용하여 인증합니다.

## 엔드포인트

### User Controller

- `GET /szs/me`: 로그인한 회원의 정보를 조회합니다.
- `POST /szs/scrap`: 회원세액 정보를 스크랩합니다.

### Auth Controller

- `POST /szs/login`: 사용자 로그인을 처리하고 JWT 토큰을 발급합니다.
- `POST /szs/reissuance`: 기존 토큰을 이용해 새로운 토큰을 발급합니다.
- `POST /szs/signup`: 새로운 회원을 등록합니다.
- `DELETE /szs/signout`: 로그아웃 기능을 제공합니다.

## 프로젝트 구조

이 프로젝트는 주요 패키지로 `com.jobis.tax.application.controller`, `com.jobis.tax.application.request`, `com.jobis.tax.application.response`, `com.jobis.tax.application.security.dto`, `com.jobis.tax.application.service` 등이 있습니다.

해당 프로젝트는 멀티모듈을 염두해두고 구성한 패키지 레이어 입니다.
자세한 사항은 블로그에 정리된 내용을 공유하도록 하겠습니다.

https://angryfullstack.tistory.com/53

## User 서비스
Spring 2.7.10 버전과 Swagger2 가 안되서 SpringDoc설정하면서 Swagger3 버전에 나올수있도록 다양한 삽질을 경험하였습니다.

가장 어려웠던 부분은 로그인후 /me 에서 Bearer 이부분 swagger2는 bearer 인데 대소문자구별때문에 6시간이 날라가네요 ㅜㅡㅜ

## Auth 서비스
spring security 에서 제공하는 인증 처리 기능을 이용해서 작업하였습니다.

Springboot 프레임워크에서 가장 보편적으로 사용되고 다양한 End point가 있어 Security 사용하였습니다.
러닝커브가 높은편이라서 항상 만들때마다 쉽지않지만 구성하고 성공하면 재미있지만 과정은  호락호락하지 않다는 생각입니다.

https://angryfullstack.tistory.com/58

## RefundCalculation 서비스
`RefundCalculation` 서비스는 세금 환급 정보를 계산하는 데 사용되는 클래스입니다. 이 서비스는 다양한 세금 공제 항목을 사용하여 최종 세액을 계산하고, 퇴직연금 세액 공제를 처리합니다.


## Scrap 서비스
`userInfoScrap` 메소드는 유저 이름,주민등록번호 기준으로 약 1초-20초까지 Response가 느려질수있는 코드입니다.

한글로된 key Json 그리고 간단하지 않은 구조라서 feignClient, webClient, objectmapper 등 고려하였지만
일주일이라는 시간에 해당부분 가장 모놀리딕 하게 구현하게 되었습니다.
리펙토링이 점진적으로 필요하다고 생각되는 부분입니다.

해당코드에서는 최초 한번 요청하고 DB에 저장하고 caffeineCacheManager를 이용해서 Cache를 생성한뒤 
그다음 처리는 TaxInformation Object를 캐쉬에 데이터가 존재하면 캐쉬로 전달하게됩니다. 

해당 처리로 HttpClient I/O, JPA H2 I/O 2가지를 SKIP하게되어 성능상 가장오래걸리는 부분을 개선하였습니다.
정책이나 운영시 고려해야되는 부분이지만 현재는 이렇게 구현하였습니다.
캐쉬 데이터는 10분으로 제약걸어둔상태입니다.

로컬캐쉬로 ehcache를 고려하였는데 caffeineCache 가 성능적으로 뛰어난 지표를 제공하여 사용하게되었습니다.
서비스 운영환경에서는 안정도가 우선이기때문에 chcache를 대체할수있는지는 
다양한 케이스에 단점이 테스트되고난뒤 사용되는것이 필요하다는 판단입니다.


## 주민등록번호 암복호화
jpa 에서 제공되는 @Converter 사용하였습니다.
해당 코드 장점은 간단하고 직관적이고 복잡도가 줄어듭니다.

```java
@Column(name="reg_no")
@Convert(converter = StringCryptoConverter.class)
private String regNo;
```

이렇게하면 별도 비지니스 로직처리없이 사용 가능합니다.

## DB스키마
```mysql
DROP TABLE IF EXISTS `refresh_tokens`;
CREATE TABLE `refresh_tokens` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id_token` (`user_id`,`token`) USING BTREE,
  KEY `idx_token` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tax_information`;
CREATE TABLE `tax_information`
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `calculated_tax` bigint(20) NOT NULL,
  `insurance_premium` bigint(20) NOT NULL,
  `education_expense` bigint(20) NOT NULL,
  `donation` bigint(20) NOT NULL,
  `medical_expense` bigint(20) NOT NULL,
  `retirement_pension` bigint(20) NOT NULL,
  `total_salary` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `roles` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `gender` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `reg_no` varchar(255) NOT NULL,
  `nickname` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(50) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

```


### 주요 기술 스택

- Spring Boot
- Spring Security
- Hibernate Validator
- Lombok
- H2 Database
- SpringDoc OpenAPI UI
- caffeineCache
- Httpclient
- JSONObject
- Stream 콜렉션 

## 설정

이 프로젝트의 주요 설정은 `application.yml` 파일에서 확인할 수 있습니다.

이 설정 파일에서는 다음과 같은 내용을 설정할 수 있습니다.

- OpenAPI 설정 파일
- JPA 설정
- H2 데이터베이스 설정
- 데이터 소스 설정
- SpringDoc 설정
- JWT 설정
- 암호화 키 및 벡터 설정 (수정필요)

## 빌드 및 실행 방법

프로젝트는 Gradle을 사용하여 빌드할 수 있습니다.
### java 11 설정
<img width="945" alt="스크린샷 2023-04-06 오후 11 10 02" src="https://user-images.githubusercontent.com/3292892/230403168-b2a00d49-4d44-44dd-a48e-14cbc2510c05.png">

### h2 DB
- mac 기준 : ~/h2db/tax
- id/pw 없음
- Embedded
- AUTO_SERVER=true 여러개 접근가능옵션

<img width="693" alt="스크린샷 2023-04-06 오후 11 11 05" src="https://user-images.githubusercontent.com/3292892/230403412-da4a57c1-6535-4f17-aeb3-b6281025dc29.png">

### Run 하면 홍길동, 김둘리 저장되게 설정하였습니다. 참고하세요!!
gradle 구동시 Test case 로직 controller 부터 확인되게 작업하였습니다.
<img width="718" alt="스크린샷 2023-04-06 오후 11 14 20" src="https://user-images.githubusercontent.com/3292892/230404424-2f2151f2-03b5-4164-a837-a897714dadea.png">

###h2 web console 접근허용 주민번호 암호화 된 Row 확인
http://localhost:8080/h2/

![스크린샷 2023-04-06 오후 11 19 12](https://user-images.githubusercontent.com/3292892/230405724-6d5531c5-a858-4dee-a165-38f0c8a58de1.png)

### Spring Doc Swagger-ui
http://localhost:8080/swagger-ui/index.html

![스크린샷 2023-04-06 오후 11 21 20](https://user-images.githubusercontent.com/3292892/230406324-c9841294-6229-42b0-be10-bf6031a1b064.png)

### 회원가입
Request DTO
```json
{
  "name": "마징가",
  "nickname": "ma",
  "password": "abcde가A1!",
  "phoneNumber": "01038183131",
  "email": "ma@gmail.com",
  "regNo": "8806012455116",
  "gender": "MAIL"
}
```
![스크린샷 2023-04-06 오후 11 27 08](https://user-images.githubusercontent.com/3292892/230407985-28b4e82a-d77e-49d8-a97e-39dc870d18c8.png)

![스크린샷 2023-04-06 오후 11 28 30](https://user-images.githubusercontent.com/3292892/230408316-55943c01-e7ef-41e8-8edd-1abaf1923ee3.png)

### 로그인 인증토근 JWT 획득
Request DTO
```json
{
  "email": "hong@gmail.com",
  "password": "abcde가A1!"
}
```
![스크린샷 2023-04-06 오후 11 23 06](https://user-images.githubusercontent.com/3292892/230406912-f5a38cae-7ced-4257-b89b-4fcdbe51d5cc.png)

### 회원정보 조회
인증토큰 header 등록
```json
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTWlMiLCJhdXRoIjoiVVNFUiIsInVpZCI6MSwiZXhwIjoxNjgwNzkyNzcxfQ.VThskKbxGhdAgjxXJAYwbwYPDekSVleLzWwettanemY
```
![스크린샷 2023-04-06 오후 11 30 10](https://user-images.githubusercontent.com/3292892/230409334-05da0e62-a5c4-4356-9078-2bb02e94f8e8.png)
![스크린샷 2023-04-06 오후 11 31 17](https://user-images.githubusercontent.com/3292892/230409531-32167cfa-ddf0-4144-884f-2a51f426ee44.png)

### 회원정보 스크랩
최초1회 key정보 주민등록번호 Cache 생성및 db등록 2957ms 응답시간
![스크린샷 2023-04-06 오후 11 32 07](https://user-images.githubusercontent.com/3292892/230410053-5def5fd8-c663-4fbb-8746-8bf9f3063bf7.png)
Cache 존재하면 캐쉬로 처리 86ms 응답시간
![스크린샷 2023-04-06 오후 11 32 15](https://user-images.githubusercontent.com/3292892/230410687-f08d8cbb-bb8c-4077-98b4-e72a8898d008.png)


프로젝트 설명 및 동작하는 기능에 대해서 공유 드립니다.
감사합니다.