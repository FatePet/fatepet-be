# 🐾 FatePet Backend 배포 가이드

이 문서는 `AWS SAM CLI`를 이용하여 Spring Boot 애플리케이션을 **AWS Lambda**에 배포하는 전체 과정을 설명합니다.  

---

## 🛠️ 1. 사전 설치

### 🍎 macOS

```bash
brew install awscli
brew install aws-sam-cli
```

### 🪟 Windows

- [Java 17 설치 (Temurin 권장)](https://adoptium.net/temurin/releases/?version=17)
- [Gradle 설치](https://gradle.org/install/)
- [AWS CLI 설치](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-windows.html)
- [SAM CLI 설치](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli-windows.html)

### ✅ 설치 확인 명령어

```bash
java -version
gradle -v
aws --version
sam --version
```

---

## 2. AWS 인증 설정

### 전달받은 Access Key 사용

다음 정보를 전달받았다고 가정합니다:

- **Access Key ID**
- **Secret Access Key**

아래 명령어로 로컬 AWS CLI에 인증을 설정합니다:

```bash
aws configure
```

입력 예시:

```
AWS Access Key ID [None]: AKIAIOSFODNN7EXAMPLE
AWS Secret Access Key [None]: wJalrXUtQnFEMEI/K7MDEWENG/bPxRfiCYEXAMPLEKEY
Default region name [None]: ap-northeast-2
Default output format [None]: json
```

---

## 🚀 3. SAM CLI로 Lambda 배포

### 🔧 1. SAM Build

```bash
sam build
```

### 🛠️ 2. 배포 (초기만 `--guided`)

```bash
sam deploy --guided
```

초기 실행 시, 아래 항목을 입력합니다:

```
Stack Name [fatepet-stack]: fatepet-stack
AWS Region [ap-northeast-2]: ap-northeast-2
Parameter DbHost []: fatepet-db.xxxxxxxx.ap-northeast-2.rds.amazonaws.com
Parameter DbPort [3306]:
Parameter DbName []: fatepet
Parameter DbUser []: admin
Parameter DbPassword []: ********
Confirm changeset [Y/n]: y
Allow SAM CLI IAM role creation [Y/n]: y
Save arguments to configuration file [Y/n]: y
```

이후엔 `sam deploy` 만으로 재배포 가능합니다.

---

## ⚙️ 4. 환경 변수 전달 방법

`template.yaml`에 정의된 `Parameters` 항목에 맞게 `sam deploy --guided` 시점에 직접 입력합니다.

환경변수 목록:
- `DbHost`: RDS 엔드포인트
- `DbPort`: 보통 3306 (MySQL)
- `DbName`: DB 이름
- `DbUser`: 사용자명
- `DbPassword`: 비밀번호

💡 비밀번호는 git이나 콘솔에 노출하지 마세요.

---

## 🧪 5. 로컬 테스트 방법

### 📦 SAM Local API 서버 실행

```bash
sam local start-api
```

브라우저나 Postman에서 테스트 가능:

```
http://localhost:3000/your-api-path
```

### 📁 샘플 이벤트로 Lambda 직접 테스트

```bash
sam local invoke PetFunction --event events/event.json
```

> `events/event.json` 파일에 요청 페이로드를 JSON 형식으로 작성하세요.


