function run() {
    ./gradlew run
}

function package() {
    ./gradlew distTar
}

function build() {
    ./gradlew --no-daemon shadowJar
}

function test() {
    ./gradlew test
}

function set_env() {
    sdk env
}

function install_env() {
    sdk env install
}