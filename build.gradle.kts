import java.util.Properties

plugins {
  java
  jacoco
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.org.sonarqube)
  // jhipster-needle-gradle-plugins
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

jacoco {
  toolVersion = libs.versions.jacoco.get()
}

val sonarProperties = Properties()
File("sonar-project.properties").inputStream().use { inputStream ->
    sonarProperties.load(inputStream)
}

sonarqube {
  properties {
    sonarProperties.forEach { (key, value) ->
      val valueList = (value as String).split(",").map { it.trim() }
      property(key as String, valueList)
    }
  }
}

defaultTasks "bootRun"

springBoot {
  mainClass = "com.mycompany.myapp.JhipsterSampleApplicationApp"
}

// jhipster-needle-gradle-plugins-configurations

repositories {
  mavenCentral()
  // jhipster-needle-gradle-repositories
}

group = "com.mycompany.myapp"
version = "0.0.1-SNAPSHOT"

ext {
  // jhipster-needle-gradle-properties
}

dependencies {
  implementation(platform(libs.spring.boot.dependencies))
  implementation(libs.spring.boot.starter)
  implementation(libs.spring.boot.configuration.processor)
  implementation(libs.commons.lang3)
  implementation(libs.spring.boot.starter.validation)
  implementation(libs.spring.boot.starter.web)
  // jhipster-needle-gradle-dependencies
  testImplementation(libs.spring.boot.starter.test)
  testImplementation(libs.reflections)

  // jhipster-needle-gradle-test-dependencies
}

tasks.jacocoTestReport {
  dependsOn("test", "integrationTest")
  reports {
    xml.required.set(true)
    html.required.set(false)
  }
}

tasks.test {
  filter {
    includeTestsMatching("*Test.*")
    excludeTestsMatching("*IT.*")
  }
  useJUnitPlatform()
  finalizedBy("jacocoTestReport")
}

val integrationTest = task<Test>("integrationTest") {
  description = "Runs integration tests."
  group = "verification"
  shouldRunAfter("test")
  filter {
    includeTestsMatching("*IT.*")
    excludeTestsMatching("*Test.*")
  }
  useJUnitPlatform()
}
