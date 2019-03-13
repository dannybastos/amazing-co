FROM dannybastos/openjdk8

MAINTAINER Danny Bastos <danny.bastos.br@gmail.com>

WORKDIR /app

COPY . /app

RUN ./gradlew clean bootJar

RUN mv /app/build/libs/amazing-co*.jar /app/app.jar
ENV SPRING_PROFILE prod

EXPOSE 8080

ENTRYPOINT java -jar -Dspring.profiles.active=$SPRING_PROFILE  /app/app.jar
