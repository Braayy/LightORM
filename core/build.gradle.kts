dependencies {
    val hikariVersion = "3.4.5"
    val h2Version = "1.4.200"

    implementation("com.zaxxer:HikariCP:$hikariVersion")
    testImplementation("com.h2database:h2:$h2Version")
}