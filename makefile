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