dependencies {
    val javapoetVersion = "1.13.0"
    val mockitoVersion = "3.7.7"

    implementation(project(":core"))

    implementation("com.squareup:javapoet:$javapoetVersion")

    testImplementation("org.mockito:mockito-core:$mockitoVersion")
}
