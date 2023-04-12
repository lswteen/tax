# 베이스 이미지 설정
FROM openjdk:11-jre-slim

# 저자 정보
LABEL maintainer="lswteen@gmail.com"

# 프로젝트의 실행 파일 이름 설정
ARG JAR_FILE=build/libs/*.jar

# 실행 파일을 컨테이너에 추가
COPY ${JAR_FILE} app.jar

# 컨테이너의 실행 명령 정의
ENTRYPOINT ["java","-jar","/app.jar"]
