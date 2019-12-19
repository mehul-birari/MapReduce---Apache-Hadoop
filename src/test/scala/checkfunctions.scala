import com.typesafe.config.{Config, ConfigFactory}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test


object checkfunctions{
  //val Config = new Configuration
  val Config: Config = ConfigFactory.load("application.conf")


  class checkfunctions {
    @Test def checkConfig(): Unit = {
      val cc = new checkfunctions
      assertNotNull(Config)
    }
  }



}
