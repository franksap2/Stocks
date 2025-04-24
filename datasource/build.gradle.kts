plugins {
    alias(libs.plugins.android.library)
}

apply {
    from("$rootDir/gradle/conventions/hilt-conventions.gradle")
    from("$rootDir/gradle/conventions/android-conventions.gradle")
}

android {
    namespace = "com.fransap2.finance.datasource"
}

dependencies {
    implementation(libs.gson)
}