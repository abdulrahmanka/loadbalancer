plugins {
	java
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.slf4j:slf4j-api:2.0.12")
	implementation(project(":common"))
	testImplementation("io.projectreactor:reactor-test")
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
