# Java 21 및 Alpine 리눅스 기반 (가볍고 빠름)
FROM eclipse-temurin:21-jdk-alpine

# 빌드 결과물인 jar 파일을 컨테이너 내부로 복사
# build.gradle의 version = '0.0.1-SNAPSHOT' 설정을 반영.
COPY ./build/libs/*-0.0.1-SNAPSHOT.jar project.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "project.jar"]