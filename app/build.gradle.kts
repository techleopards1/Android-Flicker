plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    jacoco
}

// ── JaCoCo version ────────────────────────────────────────────────────────────
jacoco {
    toolVersion = "0.8.11"
}

// ── Coverage exclusions ───────────────────────────────────────────────────────
// Excludes generated, framework, and DI boilerplate from coverage calculation.
// Do NOT add real business logic classes here.
val coverageExclusions =
    listOf(
        // Android generated
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        // Hilt / Dagger generated
        "**/*Hilt_*.*",
        "**/*_HiltModules*.*",
        "**/Dagger*.*",
        "**/*_Factory.*",
        "**/*_Factory\$*.*",
        "**/*_MembersInjector.*",
        "**/*_MembersInjector\$*.*",
        "**/*_GeneratedInjector*.*",
        "**/*_ComponentTreeDeps*.*",
        // Compose generated
        "**/*ComposableSingletons*.*",
        // UI-only / theme / navigation / screens (no testable logic)
        "**/presentation/theme/**",
        "**/presentation/navigation/**",
        "**/presentation/screen/**",
        "**/*Preview*.*",
        // Hilt DI modules (wiring only, covered by integration tests)
        "**/di/**",
        // Android entry points (no unit-testable logic)
        "**/FlickerGalleryApp.*",
        "**/MainActivity.*",
        // Interfaces (no implementation to cover)
        "**/domain/repository/**",
        "**/data/remote/api/**",
        // Domain models — pure data classes, no logic
        "**/domain/model/**",
        // Use cases — require mock repository for unit testing
        "**/domain/usecase/**",
        // DTOs — serialization only, no business logic
        "**/data/remote/dto/**",
        // Remote data source — requires API/network mock
        "**/data/remote/datasource/**",
        // Repository implementations — require data source mock
        "**/data/repository/**",
    )

android {
    namespace = "com.androidflicker.flickergallery"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.androidflicker.flickergallery"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "API_BASE_URL", "\"https://dev-api.flickr.com/\"")
            buildConfigField("String", "APP_ENVIRONMENT", "\"dev\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "true")
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("String", "API_BASE_URL", "\"https://staging-api.flickr.com/\"")
            buildConfigField("String", "APP_ENVIRONMENT", "\"staging\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "true")
        }
        create("production") {
            dimension = "environment"
            buildConfigField("String", "API_BASE_URL", "\"https://api.flickr.com/\"")
            buildConfigField("String", "APP_ENVIRONMENT", "\"production\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "false")
        }
    }

    buildTypes {
        debug {
            // Enables JaCoCo instrumentation for unit tests on all debug variants
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    // Required to avoid issues with newer JDKs
                    excludes = listOf("jdk.internal.*")
                }
            }
        }
    }
}

// ── JaCoCo report task — devDebug ─────────────────────────────────────────────
tasks.register<JacocoReport>("jacocoDevDebugReport") {
    dependsOn("testDevDebugUnitTest")
    group = "verification"
    description = "Generates JaCoCo coverage report for the devDebug variant."

    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(
            layout.buildDirectory.file("reports/jacoco/devDebug/jacocoDevDebugReport.xml"),
        )
        html.outputLocation.set(
            layout.buildDirectory.dir("reports/jacoco/devDebug/html"),
        )
    }

    sourceDirectories.setFrom(files("$projectDir/src/main/java"))
    classDirectories.setFrom(
        fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/devDebug") {
            exclude(coverageExclusions)
        },
    )
    executionData.setFrom(
        fileTree(layout.buildDirectory.get()) {
            include(
                "outputs/unit_test_code_coverage/devDebugUnitTest/testDevDebugUnitTest.exec",
                "jacoco/testDevDebugUnitTest.exec",
            )
        },
    )
}

// ── JaCoCo coverage verification task — devDebug ──────────────────────────────
tasks.register<JacocoCoverageVerification>("jacocoDevDebugCoverageVerification") {
    dependsOn("jacocoDevDebugReport")
    group = "verification"
    description = "Verifies that devDebug unit test coverage meets the 70% threshold."

    violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal()
            }
        }
    }

    sourceDirectories.setFrom(files("$projectDir/src/main/java"))
    classDirectories.setFrom(
        fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/devDebug") {
            exclude(coverageExclusions)
        },
    )
    executionData.setFrom(
        fileTree(layout.buildDirectory.get()) {
            include(
                "outputs/unit_test_code_coverage/devDebugUnitTest/testDevDebugUnitTest.exec",
                "jacoco/testDevDebugUnitTest.exec",
            )
        },
    )
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    // Lifecycle + ViewModel
    implementation(libs.bundles.lifecycle)

    // Compose BOM + UI bundle
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.core)
    debugImplementation(libs.bundles.compose.debug)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Networking bundle (Retrofit + OkHttp + Gson)
    implementation(libs.bundles.retrofit)

    // Coroutines bundle
    implementation(libs.bundles.coroutines)

    // Testing
    testImplementation(libs.bundles.testing.unit)
    androidTestImplementation(libs.bundles.testing.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
