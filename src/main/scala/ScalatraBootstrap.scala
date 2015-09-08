import com.incra.app._
import com.incra.init.ServiceConfiguration
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  implicit lazy val bindingModule =
    ServiceConfiguration

  override def init(context: ServletContext) {
    context.mount(new MainServlet, "/*")
  }
}
