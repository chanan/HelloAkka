// @SOURCE:/Users/ubrauch/java/helloakka/conf/routes
// @HASH:db805f4cf1bfca1d78c9c5cc5c7f9aae03045e6c
// @DATE:Wed Aug 27 16:28:53 EDT 2014

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.Router.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._
import _root_.controllers.Assets.Asset
import _root_.play.libs.F

import Router.queryString


// @LINE:22
// @LINE:18
// @LINE:17
// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers {

// @LINE:22
class ReverseAssets {


// @LINE:22
def at(file:String): Call = {
   implicit val _rrc = new ReverseRouteContext(Map(("path", "/public")))
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                        

}
                          

// @LINE:17
class ReverseGradebook {


// @LINE:17
def course(courseId:String): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "gradebook/course/" + implicitly[PathBindable[String]].unbind("courseId", dynamicString(courseId)))
}
                        

}
                          

// @LINE:18
class ReverseGradebookWithStatus {


// @LINE:18
def course(courseId:String): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "gradebookstatus/course/" + implicitly[PathBindable[String]].unbind("courseId", dynamicString(courseId)))
}
                        

}
                          

// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
class ReverseApplication {


// @LINE:9
def doGlobalWorkWithName(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "doGlobalWorkWithName")
}
                        

// @LINE:12
def serviceCallError(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "serviceCallError")
}
                        

// @LINE:14
def FaultTolerantComplexServiceActor(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "FaultTolerantComplexServiceActor")
}
                        

// @LINE:7
def doWork(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "dowork")
}
                        

// @LINE:8
def doGlobalWork(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "doGlobalWork")
}
                        

// @LINE:13
def complexService(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "complexService")
}
                        

// @LINE:6
def index(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix)
}
                        

// @LINE:10
def serviceCallTell(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "serviceCallTell")
}
                        

// @LINE:11
def serviceCallAsk(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "serviceCallAsk")
}
                        

}
                          
}
                  


// @LINE:22
// @LINE:18
// @LINE:17
// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.javascript {
import ReverseRouteContext.empty

// @LINE:22
class ReverseAssets {


// @LINE:22
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        

}
              

// @LINE:17
class ReverseGradebook {


// @LINE:17
def course : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Gradebook.course",
   """
      function(courseId) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "gradebook/course/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("courseId", encodeURIComponent(courseId))})
      }
   """
)
                        

}
              

// @LINE:18
class ReverseGradebookWithStatus {


// @LINE:18
def course : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.GradebookWithStatus.course",
   """
      function(courseId) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "gradebookstatus/course/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("courseId", encodeURIComponent(courseId))})
      }
   """
)
                        

}
              

// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
class ReverseApplication {


// @LINE:9
def doGlobalWorkWithName : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.doGlobalWorkWithName",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "doGlobalWorkWithName"})
      }
   """
)
                        

// @LINE:12
def serviceCallError : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.serviceCallError",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "serviceCallError"})
      }
   """
)
                        

// @LINE:14
def FaultTolerantComplexServiceActor : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.FaultTolerantComplexServiceActor",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "FaultTolerantComplexServiceActor"})
      }
   """
)
                        

// @LINE:7
def doWork : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.doWork",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "dowork"})
      }
   """
)
                        

// @LINE:8
def doGlobalWork : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.doGlobalWork",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "doGlobalWork"})
      }
   """
)
                        

// @LINE:13
def complexService : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.complexService",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "complexService"})
      }
   """
)
                        

// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        

// @LINE:10
def serviceCallTell : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.serviceCallTell",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "serviceCallTell"})
      }
   """
)
                        

// @LINE:11
def serviceCallAsk : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.serviceCallAsk",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "serviceCallAsk"})
      }
   """
)
                        

}
              
}
        


// @LINE:22
// @LINE:18
// @LINE:17
// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.ref {


// @LINE:22
class ReverseAssets {


// @LINE:22
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      

}
                          

// @LINE:17
class ReverseGradebook {


// @LINE:17
def course(courseId:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Gradebook.course(courseId), HandlerDef(this.getClass.getClassLoader, "", "controllers.Gradebook", "course", Seq(classOf[String]), "GET", """ Cassandra""", _prefix + """gradebook/course/$courseId<[^/]+>""")
)
                      

}
                          

// @LINE:18
class ReverseGradebookWithStatus {


// @LINE:18
def course(courseId:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.GradebookWithStatus.course(courseId), HandlerDef(this.getClass.getClassLoader, "", "controllers.GradebookWithStatus", "course", Seq(classOf[String]), "GET", """""", _prefix + """gradebookstatus/course/$courseId<[^/]+>""")
)
                      

}
                          

// @LINE:14
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
class ReverseApplication {


// @LINE:9
def doGlobalWorkWithName(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.doGlobalWorkWithName(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "doGlobalWorkWithName", Seq(), "GET", """""", _prefix + """doGlobalWorkWithName""")
)
                      

// @LINE:12
def serviceCallError(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.serviceCallError(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "serviceCallError", Seq(), "GET", """""", _prefix + """serviceCallError""")
)
                      

// @LINE:14
def FaultTolerantComplexServiceActor(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.FaultTolerantComplexServiceActor(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "FaultTolerantComplexServiceActor", Seq(), "GET", """""", _prefix + """FaultTolerantComplexServiceActor""")
)
                      

// @LINE:7
def doWork(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.doWork(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "doWork", Seq(), "GET", """""", _prefix + """dowork""")
)
                      

// @LINE:8
def doGlobalWork(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.doGlobalWork(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "doGlobalWork", Seq(), "GET", """""", _prefix + """doGlobalWork""")
)
                      

// @LINE:13
def complexService(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.complexService(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "complexService", Seq(), "GET", """""", _prefix + """complexService""")
)
                      

// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      

// @LINE:10
def serviceCallTell(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.serviceCallTell(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "serviceCallTell", Seq(), "GET", """""", _prefix + """serviceCallTell""")
)
                      

// @LINE:11
def serviceCallAsk(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.serviceCallAsk(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "serviceCallAsk", Seq(), "GET", """""", _prefix + """serviceCallAsk""")
)
                      

}
                          
}
        
    