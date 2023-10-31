@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.jnyakush.core"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {


    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.ktx)
    api(libs.activity.compose)
    api(platform(libs.compose.bom))
    api(libs.compose.ui)
    api(libs.compose.ui.graphics)
    api(libs.compose.ui.tooling.preview)
    api(libs.compose.material3)

    // Retrofit
    api(libs.retrofit)
    api(libs.retrofit.converter.gson)

    // Interceptors
    api(libs.okhttp)
    api(libs.logging.interceptor)


    debugApi(libs.library)
    releaseApi(libs.library.no.op)

    // Navigation
    api(libs.navigation)

    // Koin for Di
    api(libs.koin.android)
    api(libs.koin.core)
    api( libs.koin.androidx.compose)

    // Logging Libraries
    api(libs.timber)

    api(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.expresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling.testing)
    debugImplementation(libs.ui.test.manifest)
}