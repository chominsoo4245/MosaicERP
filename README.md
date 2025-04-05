# Mini ERP System (MSA Architecture Demo)

이 프로젝트는 실제 업무에서 사용되는 ERP 시스템을 마이크로서비스 기반으로
보여주기 위한 **개발자용 데모 프로젝트** 입니다.

---


## 🚀 주요 특징


- ✅ 도메인별 마이크로서비스 분리 (HR, 회계, 재고 등)

- ✅ Spring Boot 기반 RESTful API

- ✅ Spring Cloud Gateway를 활용한 API 게이트웨이

- ✅ Eureka를 이용한 서비스 등록

- ✅ JWT 인증 / 인가 처리

- ✅ Kafka를 통한 비동기 메세지 처리

- ✅ 주요 서비스별 개별 DB 구성

- ✅ Spring Cloud Config로 설정 중앙화

- ✅ Docker & Docker Compose로 로컬 실행 가능

- ✅ Github Action CI & CD 구현


--- 


## 👜 프로젝트 구조

```

MosaicERP  

┣- 📂 api-gateway          (API 게이트웨이)  

┣- 📂 auth-service         (인증/인가 서비스)

┣- 📂 user-service         (유저 서비스)    

┣- 📂 hr-service           (인사 관리 서비스)  

┣- 📂 inventory-service    (재고 관리 서비스)  

┣- 📂 accounting-service   (회계 서비스)  

┣- 📂 file-service         (파일 업로드 서비스)  

┣- 📂 config-server        (설정 서버)  

┣- 📂 discovery-server     (서비스 등록/발견 [Eureka])  

┗- 📂 docker               (Docker 구성 파일)

```


---


## ⚙️ 사용 기술 스택

| 분류                | 기술                                        |

|-------------------|-------------------------------------------|

| Laguage           | Java 17                                   |

| Freamwork         | Spring Boot, Spring Cloud |

| API Gateway       | Spring Cloud Gateway                      |

| Auth              | JWT, Spring Sucurity|

| DB                | PostgreSQL (서비스별 독립)|

| Message           | Kafka|

| Setting           | Spring Cloud Config|

| Service Discovery | Eureka|

| Infra             | Docker, Docker Compose, Github Action|

| API Documentation | Swagger (Springdoc OpenAPI)|

| FrontEnd | React + Vite|