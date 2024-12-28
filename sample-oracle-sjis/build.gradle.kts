plugins {
    kotlin("jvm") version "2.0.21"
}

group = "net.leoninja.javaoracleverification"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))

    implementation("org.jdbi:jdbi3-core:3.47.0")
    implementation("org.jdbi:jdbi3-kotlin:3.47.0")

    runtimeOnly("com.oracle.database.jdbc:ojdbc11:23.6.0.24.10")

    testImplementation(kotlin("test"))
}

val encodingJvmArgs = listOf(
    "-Dfile.encoding=UTF-8",
    "-Dsun.stdout.encoding=UTF-8",
    "-Dsun.stderr.encoding=UTF-8",
    "-Dstdout.encoding=UTF-8",
    "-Dstderr.encoding=UTF-8",
)

tasks.withType<JavaExec> {
    // JavaExec型のタスクのVM引数にエンコーディング関連のプロパティを追加する
    jvmArgs(encodingJvmArgs)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}

tasks.test {
    jvmArgs(encodingJvmArgs)
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}