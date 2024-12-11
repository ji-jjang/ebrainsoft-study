all:
	mysql-run

mysql-run:
	docker run --name board-mysql \
	-p 3306:3306 \
	-e MYSQL_ROOT_PASSWORD=0000 \
	mysql:9.1 \
	--character-set-server=UTF8MB4 \
	--collation-server=UTF8MB4_UNICODE_CI

.phony: all mysql-run