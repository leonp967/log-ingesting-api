# Log Access Analytics
Foi utilizado o Logstash em conjunto com o Elasticsearch para realizar o tratamento, armazenamento e métricas dos logs. Quando o serviço recebe um log via REST, este é salvo no Logstash, que realiza o tratamento do timestamp para extrair a semana do ano, dia do mês, ano e minuto em que o log ocorreu. Após o tratamento isto é enviado para o Elasticsearch para ser indexado. E no momento de gerar métricas, foram utilizados os Terms Aggregations do Elasticsearch.

## Configuração utilizada no Logstash
```
input {
	http { 
          port => 8090
       }
}

filter {
  json{
    	source => "message"
	}
  date { match => ["accessTimestamp", "yyyy-MMMM-dd HH:mm:ss", "UNIX_MS"]
    	target => "@timestamp"
    	add_field => {"minute" => "%{+mm}"}
    	add_field => {"week" => "%{+w}"}
    	add_field => {"day" => "%{+dd}"}
    	add_field => {"year" => "%{+yyyy}"}
  	}
}

output {
  elasticsearch { hosts => ["localhost:9200"] }
}
```
