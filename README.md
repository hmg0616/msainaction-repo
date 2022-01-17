# msainaction-repo
참고 블로그 : https://mungyu.tistory.com/category/MSA/Spring%20Microservice%20In%20Action

## 1. 서비스 실행
각 프로젝트 빌드
> mvn clean package docker:build

## 2. 전체 프로젝트 실행
> docker-compose -f docker/common/docker-compose.yml up
