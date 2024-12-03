all: 
	up

up:
	cd ../frontend/ &&  npm run build
	docker-compose up --detach

up-build:
	docker-compose up --build --detach

down:
	docker-compose down

restart-app:
	docker-compose up --build app --detach

restart-nginx:
	docker-compose up --build nginx --detach

restart-mysql:
	docker-compose up --build mysql --detach

.PHONY: all up down restart-app restart-nginx restart-mysql
