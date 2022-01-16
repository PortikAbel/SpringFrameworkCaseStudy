FROM csabasulyok/gradle:7.2-jdk16-alpine

COPY . .

ARG profile=prod

ENV SPRING_PROFILES_ACTIVE=${profile}

RUN gradle bootjar

EXPOSE 8080

CMD [ "java", "-jar", "paim1949-extra/build/libs/paim1949-extra-1.0-SNAPSHOT.jar" ]
