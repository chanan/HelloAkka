// @SOURCE:/Users/ubrauch/java/HelloAkka/conf/routes
// @HASH:e9c80dd3117da6aebff4ff9e59194b8b5e9f58af
// @DATE:Tue Jul 08 07:49:26 EDT 2014


import play.core._
import play.core.Router._
import play.core.Router.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._
import _root_.controllers.Assets.Asset
import _root_.play.libs.F

import Router.queryString

object Routes extends Router.Routes {

import ReverseRouteContext.empty

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Application_index0_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
private[this] lazy val controllers_Application_index0_invoker = createInvoker(
controllers.Application.index(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "index", Nil,"GET", """ Home page""", Routes.prefix + """"""))
        

// @LINE:7
private[this] lazy val controllers_Application_doWork1_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("dowork"))))
private[this] lazy val controllers_Application_doWork1_invoker = createInvoker(
controllers.Application.doWork(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "doWork", Nil,"GET", """""", Routes.prefix + """dowork"""))
        

// @LINE:8
private[this] lazy val controllers_Application_doGlobalWork2_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("doGlobalWork"))))
private[this] lazy val controllers_Application_doGlobalWork2_invoker = createInvoker(
controllers.Application.doGlobalWork(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "doGlobalWork", Nil,"GET", """""", Routes.prefix + """doGlobalWork"""))
        

// @LINE:9
private[this] lazy val controllers_Application_doGlobalWorkWithName3_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("doGlobalWorkWithName"))))
private[this] lazy val controllers_Application_doGlobalWorkWithName3_invoker = createInvoker(
controllers.Application.doGlobalWorkWithName(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "doGlobalWorkWithName", Nil,"GET", """""", Routes.prefix + """doGlobalWorkWithName"""))
        

// @LINE:10
private[this] lazy val controllers_Application_serviceCallTell4_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("serviceCallTell"))))
private[this] lazy val controllers_Application_serviceCallTell4_invoker = createInvoker(
controllers.Application.serviceCallTell(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "serviceCallTell", Nil,"GET", """""", Routes.prefix + """serviceCallTell"""))
        

// @LINE:11
private[this] lazy val controllers_Application_serviceCallAsk5_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("serviceCallAsk"))))
private[this] lazy val controllers_Application_serviceCallAsk5_invoker = createInvoker(
controllers.Application.serviceCallAsk(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "serviceCallAsk", Nil,"GET", """""", Routes.prefix + """serviceCallAsk"""))
        

// @LINE:12
private[this] lazy val controllers_Application_serviceCallError6_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("serviceCallError"))))
private[this] lazy val controllers_Application_serviceCallError6_invoker = createInvoker(
controllers.Application.serviceCallError(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "serviceCallError", Nil,"GET", """""", Routes.prefix + """serviceCallError"""))
        

// @LINE:13
private[this] lazy val controllers_Application_complexService7_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("complexService"))))
private[this] lazy val controllers_Application_complexService7_invoker = createInvoker(
controllers.Application.complexService(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "complexService", Nil,"GET", """""", Routes.prefix + """complexService"""))
        

// @LINE:16
private[this] lazy val controllers_Assets_at8_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
private[this] lazy val controllers_Assets_at8_invoker = createInvoker(
controllers.Assets.at(fakeValue[String], fakeValue[String]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
        
def documentation = List(("""GET""", prefix,"""controllers.Application.index()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """dowork""","""controllers.Application.doWork()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """doGlobalWork""","""controllers.Application.doGlobalWork()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """doGlobalWorkWithName""","""controllers.Application.doGlobalWorkWithName()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """serviceCallTell""","""controllers.Application.serviceCallTell()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """serviceCallAsk""","""controllers.Application.serviceCallAsk()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """serviceCallError""","""controllers.Application.serviceCallError()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """complexService""","""controllers.Application.complexService()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]]
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Application_index0_route(params) => {
   call { 
        controllers_Application_index0_invoker.call(controllers.Application.index())
   }
}
        

// @LINE:7
case controllers_Application_doWork1_route(params) => {
   call { 
        controllers_Application_doWork1_invoker.call(controllers.Application.doWork())
   }
}
        

// @LINE:8
case controllers_Application_doGlobalWork2_route(params) => {
   call { 
        controllers_Application_doGlobalWork2_invoker.call(controllers.Application.doGlobalWork())
   }
}
        

// @LINE:9
case controllers_Application_doGlobalWorkWithName3_route(params) => {
   call { 
        controllers_Application_doGlobalWorkWithName3_invoker.call(controllers.Application.doGlobalWorkWithName())
   }
}
        

// @LINE:10
case controllers_Application_serviceCallTell4_route(params) => {
   call { 
        controllers_Application_serviceCallTell4_invoker.call(controllers.Application.serviceCallTell())
   }
}
        

// @LINE:11
case controllers_Application_serviceCallAsk5_route(params) => {
   call { 
        controllers_Application_serviceCallAsk5_invoker.call(controllers.Application.serviceCallAsk())
   }
}
        

// @LINE:12
case controllers_Application_serviceCallError6_route(params) => {
   call { 
        controllers_Application_serviceCallError6_invoker.call(controllers.Application.serviceCallError())
   }
}
        

// @LINE:13
case controllers_Application_complexService7_route(params) => {
   call { 
        controllers_Application_complexService7_invoker.call(controllers.Application.complexService())
   }
}
        

// @LINE:16
case controllers_Assets_at8_route(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at8_invoker.call(controllers.Assets.at(path, file))
   }
}
        
}

}
     