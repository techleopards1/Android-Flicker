plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    source.setFrom(
        "app/src/main/java",
        "app/src/test/java",
        "app/src/androidTest/java",
    )
    parallel = true
    autoCorrect = false
    buildUponDefaultConfig = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html {
            required.set(true)
            outputLocation.set(file("build/reports/detekt/detekt.html"))
        }
        xml {
            required.set(true)
            outputLocation.set(file("build/reports/detekt/detekt.xml"))
        }
        sarif {
            required.set(true)
            outputLocation.set(file("build/reports/detekt/detekt.sarif"))
        }
        txt {
            required.set(false)
        }
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "11"
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        ktlint("1.3.1")
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**/*.kts")
        ktlint("1.3.1")
    }
}
