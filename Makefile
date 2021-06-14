SHELL := /bin/bash

# HELP
# This will output the help for each task
# thanks to https://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
.PHONY: help

help: ## This help.
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

.DEFAULT_GOAL := help

# APPLICATION TASKS TO RUN AND TEST
build: compose-up ## is used to compile and run tests.
	mvn clean install
	compose-down

compose-up: ## run docker-compose up
	docker-compose up

compose-down: ## run docker-compose down
	docker-compose down
