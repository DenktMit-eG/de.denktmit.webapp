package de.denktmit.webapp.testutils

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class PostgresTestContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

  companion object {
    private const val DEFAULT_HOST: String = "localhost"
    private const val DEFAULT_PORT: Int = 35432
    private const val DEFAULT_NAME: String = "webapp"
    private const val DEFAULT_USERNAME: String = "db-it-user"
    private const val DEFAULT_PASSWORD: String = "db-it-pass"
  }

  override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
    val dbHost = System.getenv("POSTGRES_HOST")?: DEFAULT_HOST
    val dbPort = System.getenv("POSTGRES_PORT")?.toIntOrNull() ?: DEFAULT_PORT
    val dbName = System.getenv("POSTGRES_DB")?: DEFAULT_NAME
    val dbUser = System.getenv("POSTGRES_USER")?: DEFAULT_USERNAME
    val dbPassword = System.getenv("POSTGRES_PASSWORD")?: DEFAULT_PASSWORD
    val dbUrl = dbUrl(dbHost, dbPort, dbName)
    TestPropertyValues.of(
      "spring.datasource.url=$dbUrl",
      "spring.datasource.username=$dbUser",
      "spring.datasource.password=$dbPassword"
    ).applyTo(configurableApplicationContext.environment)
    resetDBWithFlyway(dbUrl, dbUser, dbPassword)
  }

  private fun resetDBWithFlyway(dbUrl: String, dbUser: String, dbPassword: String) {
    val config = ClassicConfiguration().apply {
      isCleanDisabled=false
      setLocationsAsStrings("")
      setDataSource(dbUrl, dbUser, dbPassword)
    }
    Flyway(config).apply {
      clean()
    }
  }

  private fun dbUrl(host: String, port: Int, dbName: String): String =
    "jdbc:postgresql://$host:$port/$dbName?loggerLevel=OFF"

}
