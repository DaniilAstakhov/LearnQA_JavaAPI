FROM maven:3.9.5
WORKDIR /tests
COPY . .
CMD mvn clen test