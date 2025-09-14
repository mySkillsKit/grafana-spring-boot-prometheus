## Prometheus Grafana Loki API
```aiignore
mvn clean package -DskipTests

docker compose up -d

```
```aiignore
https://grafana.com/grafana/dashboards/

http://localhost:3000 → Grafana (логин: admin / admin)
http://localhost:9090 → Prometheus UI
http://localhost:3100 → Loki API
http://localhost:8080/actuator/prometheus → метрики backend
http://localhost:8080/ping → тестовый endpoint
http://localhost:8080/status/200?secondsSleep=2
http://localhost:8080/swagger-ui.html
```
## Нагрузочное тестирование с ab:

```
ab -k -c 5 -n 20000 'http://localhost:8080/ping' & \
ab -k -c 5 -n 2000 'http://localhost:8080/status/400' & \
ab -k -c 5 -n 3000 'http://localhost:8080/status/409' & \
ab -k -c 5 -n 5000 'http://localhost:8080/status/500' & \
ab -k -c 50 -n 5000 'http://localhost:8080/status/200?secondsSleep=1' & \
ab -k -c 50 -n 2000 'http://localhost:8080/status/200?secondsSleep=2'
```

## Пример дашборда
Находится в папке /grafana/example-dashboard.json
