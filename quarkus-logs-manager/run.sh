docker-compose -p quarkus-logs-manager up -d postgres
 ./mvnw package
 docker-compose build & docker-compose up