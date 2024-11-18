all:
	up
up:
	docker-compose --file ./docker-compose.yml up --detach --build
stop:
	docker-compose --file ./docker-compose.yml stop
down:
	docker-compose --file ./docker-compose.yml down --remove-orphans

.PHONY: all up stop down