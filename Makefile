
up_dev:
	docker compose -f docker-compose_dev.yml up -d
down_dev:
	docker compose -f docker-compose_dev.yml down

up_test:
	docker compose -f docker-compose_test.yml up -d
down_test:
	docker compose -f docker-compose_test.yml down

up_prod:
	docker compose -f docker-compose_production.yml up -d
down_prod:
	docker compose -f docker-compose_production.yml down