run:
	make build
	./app/build/install/app/bin/app

package:
	./gradlew distTar

build:
	rm -rf ./build
	rm -rf ./app/build
	./gradlew --no-daemon installDist

test:
	./gradlew test

set-env:
	sdk env

install-env:
	sdk env install

test-coverage:
	./gradlew jacocoTestReport

install-hooks:
	bash ./scripts/install-hooks.sh

check-coverage:
	./gradlew jacocoTestReport
	bash ./scripts/check-coverage.sh
	open ./app/build/reports/jacoco/test/html/index.html