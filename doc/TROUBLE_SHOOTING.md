# Trouble Shooting
프로젝트를 진행하면서 발생한 문제점들과 해결법 서술합니다.

## Spring Boot 와 Docker Mysql 연동 이슈 해결
1. docker-compose.yml 작성
2. docker-compose up -d 사용하여 MySQL 컨테이너 실행
3. Mysql 컨테이너에 접속하여 ecommerce db 생성
4. application.yml에 mysql 코드 추가
5. Datagrip에 연결
<br>

Datagrip 연결하는 과정에서 문제가 발생했다.<br>
User와 Password를 입력하고 Test Connection을 시도 했는데 <br>
예전에 공부하면서 ubuntu 환경에서 설치한 mysql이 계속 연결되었다. <br>
그래서 MySQL 컨테이너가 잘 작동하는지 확인하고, 컨테이너 IP 주소를 확인하여<br>
명령어) docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql_container <br>
Datagrip Host에 IP 주소값을 넣고 Database를 ecommerce로 지정해주었지만 이번에는 연결이 실패하여서<br>
Intellij 캐시를 제거한 후 다시 시도하니 연결이 되었다.<br>

![connection](img/db_connection.PNG)
