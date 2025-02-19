plugins {
  id("otel.javaagent-instrumentation")
}

muzzle {
  pass {
    group.set("org.eclipse.jetty")
    module.set("jetty-server")
    versions.set("[11,)")
  }
}

dependencies {
  library("org.eclipse.jetty:jetty-server:11.0.0")
  implementation(project(":instrumentation:servlet:servlet-5.0:javaagent"))
  implementation(project(":instrumentation:jetty:jetty-common:javaagent"))

  // Don't want to conflict with jetty from the test server.
  testImplementation(project(":testing-common")) {
    exclude("org.eclipse.jetty", "jetty-server")
  }

  testLibrary("org.eclipse.jetty:jetty-servlet:11.0.0")
}

otelJava {
  minJavaVersionSupported.set(JavaVersion.VERSION_11)
}
