plugins {
    id("java")
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}



dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    compileOnly("org.springframework.boot:spring-boot-starter-webflux:3.2.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.3")
    testImplementation("org.mockito:mockito-core:5.11.0")

}

tasks.test {
    useJUnitPlatform()
}