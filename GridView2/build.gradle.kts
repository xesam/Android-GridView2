plugins {
    id("com.android.library")
    id("com.vanniktech.maven.publish") version "0.35.0"
    id("signing")
}


android {
    namespace = "io.github.xesam.android.gridview2"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    compileOnly(libs.annotation)
    compileOnly(libs.recyclerview.library)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

mavenPublishing {
    coordinates("io.github.xesam", "android-gridview2", "0.0.1")
    pom {
        name.set("Android-GridView2")
        description.set("A Simple Android GridView Component")
        url.set("https://github.com/xesam/Android-GridView2")
        inceptionYear.set("2025")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("xesam")
                name.set("xesam")
                email.set("xesam@outlook.com")
                organization.set("xesam")
                organizationUrl.set("https://github.com/xesam")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/xesam/Android-GridView2.git")
            developerConnection.set("scm:git:ssh://github.com:xesam/Android-GridView2.git")
            url.set("https://github.com/xesam/Android-GridView2")
        }
    }

    publishToMavenCentral()
    signAllPublications()
}
