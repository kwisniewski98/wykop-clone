check-deps:
EXECUTABLES = docker python3
K := $(foreach exec,$(EXECUTABLES),\
        $(if $(shell which $(exec)),some string,$(error "No $(exec) in PATH")))

test: check-deps
	@echo ">> building test image"
	@docker build -t vykop-e2e  -f e2e/Dockerfile .
	@echo ">> running test container"
	@docker run -d -p 8080:8080 --name vykop-e2e vykop-e2e
	@python3 -m venv venv
	@. venv/bin/activate
	@python3 -m pip install -r e2e/tests/requirements.txt
	@cd e2e/tests && python3 -m pytest -vv * || true
	@mkdir -p out/
	@docker logs vykop-e2e  > out/container.log
	@echo "Container logs can be seen in out/container.log"
	@rm -rf venv
	@docker stop vykop-e2e && docker rm vykop-e2e

build: check-deps
	@echo ">> building image"
	@docker build -t vykop  -f deploy/Dockerfile .

deploy: build check-deps
	@echo ">> running container"
	@docker run -p 8080:8080 --name vykop vykop
