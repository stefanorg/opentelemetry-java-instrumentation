plugins {
  id("otel.javaagent-instrumentation")
  id("org.unbroken-dome.test-sets")
}

muzzle {
  fail {
    group.set("com.vaadin")
    module.set("flow-server")
    versions.set("[,2.2.0)")
  }
  pass {
    group.set("com.vaadin")
    module.set("flow-server")
    versions.set("[2.2.0,3)")
  }
  fail {
    group.set("com.vaadin")
    module.set("flow-server")
    versions.set("[3.0.0,3.1.0)")
  }
  pass {
    group.set("com.vaadin")
    module.set("flow-server")
    versions.set("[3.1.0,)")
  }
}


testSets {
  create("vaadin142Test")
  create("vaadin14LatestTest")
  create("vaadin16Test")
  create("latestDepTest") {
    dirName = "vaadinLatestTest"
  }
}

tasks {
  val vaadin142Test by existing
  val vaadin16Test by existing
  val vaadin14LatestTest by existing

  named<Test>("test") {
    dependsOn(vaadin142Test)
    dependsOn(vaadin16Test)
    if (findProperty("testLatestDeps") as Boolean) {
      dependsOn(vaadin14LatestTest)
    }
  }
}

dependencies {
  compileOnly("com.vaadin:flow-server:2.2.0")

  add("vaadin16TestImplementation", "com.vaadin:vaadin-spring-boot-starter:16.0.0")
  add("vaadin142TestImplementation", "com.vaadin:vaadin-spring-boot-starter:14.2.0")

  testImplementation(project(":instrumentation:vaadin-14.2:testing"))
  testImplementation(project(":testing-common"))

  testInstrumentation(project(":instrumentation:servlet:servlet-3.0:javaagent"))
  testInstrumentation(project(":instrumentation:servlet:servlet-javax-common:javaagent"))
  testInstrumentation(project(":instrumentation:tomcat:tomcat-7.0:javaagent"))

  add("vaadin14LatestTestImplementation", "com.vaadin:vaadin-spring-boot-starter:14.+")
  add("latestDepTestImplementation", "com.vaadin:vaadin-spring-boot-starter:+")
}
