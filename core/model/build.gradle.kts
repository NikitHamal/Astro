import java.net.URL

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.astro.storm.core.model"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

// Custom task to download Swiss Ephemeris if missing
val downloadSwissEph = tasks.register("downloadSwissEph") {
    val libDir = file("libs")
    val outputFile = file("libs/swisseph-2.10.03.jar")
    outputs.file(outputFile)
    
    doLast {
        if (!outputFile.exists()) {
            libDir.mkdirs()
            println("Downloading Swiss Ephemeris library...")
            val url = URL("http://www.th-mack.de/download/swisseph-2.00.00-01.jar")
            url.openStream().use { input: java.io.InputStream ->
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation("androidx.core:core-ktx:1.12.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material:material-icons-extended")

    // Swiss Ephemeris
    implementation(files(downloadSwissEph))
}
