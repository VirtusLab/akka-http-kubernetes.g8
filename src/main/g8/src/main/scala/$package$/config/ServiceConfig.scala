package $package$.config

import pureconfig._
import pureconfig.generic.auto._

final case class Port(number: Int) extends AnyVal
final case class Host(name: String) extends AnyVal

case class ServiceConfig(host: Host, port: Port)

object ServiceConfig {
  def load(): ServiceConfig = {
    ConfigSource.default.at("service-config").loadOrThrow[ServiceConfig]
  }
}
