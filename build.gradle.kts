plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint) apply false
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    source.setFrom(
        "app/src/main/java",
        "app/src/test/java"
    )
    parallel = true
    autoCorrect = false
}
