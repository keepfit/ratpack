package org.ratpackframework.test.groovy

import org.ratpackframework.bootstrap.RatpackServer
import org.ratpackframework.groovy.RatpackScript
import org.ratpackframework.groovy.RatpackScriptApp
import org.ratpackframework.test.InternalRatpackSpec

abstract class RatpackGroovyScriptAppSpec extends InternalRatpackSpec {

  boolean compileStatic = false
  boolean reloadable = false

  File getRatpackFile() {
    file("ratpack.groovy")
  }

  File templateFile(String path) {
    file("templates/$path")
  }

  def setup() {
    ratpackFile << "import static ${RatpackScript.name}.ratpack\n\n"
  }

  void script(String text) {
    ratpackFile.text = "import static ${RatpackScript.name}.ratpack\n\n$text"
  }

  @Override
  protected RatpackServer createServer() {
    RatpackScriptApp.ratpack(ratpackFile, dir, 0, null, compileStatic, reloadable)
  }

}
