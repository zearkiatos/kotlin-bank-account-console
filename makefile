run:
	./gradlew run --console=plain

package:
	./gradlew distTar

build:
	./gradlew --no-daemon shadowJar

test:
	./gradlew test

set-env:
	sdk env

install-env:
	sdk env install