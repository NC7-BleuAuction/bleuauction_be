FROM --platform=linux/amd64 openjdk:17-alpine

# 선택할 JAR 파일 지정
ARG SELECTED_JAR_FILE=bleuauction_be-0.0.1-SNAPSHOT.jar
ENV ACTIVE_PROFILES=local

# 선택한 JAR 파일을 컨테이너 내로 복사
COPY build/libs/${SELECTED_JAR_FILE} /app.jar

# 사용자 설정 (root 사용)
USER root

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=${ACTIVE_PROFILES}", "-Djava.security.egd=file:/dev/./random", "-jar", "/app.jar"]
