plugins {
    id("com.android.library")
    id("maven-publish")
}

publishing {
    publications {
        publications.create<MavenPublication>("aar") {
            groupId = "com.github.f1uctus"
            artifactId = "crown"
            version = "android-0.10.0"
            artifact("${buildDir}/outputs/aar/crown-release.aar")
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/f1uctus/crown")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.2.0")
    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    implementation("org.jetbrains:annotations:20.1.0")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    implementation("org.junit.platform:junit-platform-commons:1.4.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    implementation("org.junit.platform:junit-platform-engine:1.4.2")
}