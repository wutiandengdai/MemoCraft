mvn clean package dockerfile:build

docker run -p 8080:8080 wutiandengdai/memo-craft

docker login

mvn dockerfile:push
