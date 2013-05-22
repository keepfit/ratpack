package org.ratpackframework.test

import com.google.inject.Module
import org.ratpackframework.bootstrap.RatpackServer
import org.ratpackframework.bootstrap.RatpackServerBuilder
import org.ratpackframework.guice.GuiceBackedHandlerFactory
import org.ratpackframework.guice.ModuleRegistry
import org.ratpackframework.guice.internal.DefaultGuiceBackedHandlerFactory
import org.ratpackframework.handling.Handler
import org.ratpackframework.handling.Handlers
import org.ratpackframework.handling.Chain
import org.ratpackframework.util.Action

import static Handlers.chain
import static org.ratpackframework.groovy.Closures.action
import static org.ratpackframework.groovy.Closures.configure

abstract class DefaultRatpackSpec extends InternalRatpackSpec {

  Closure<?> handlersClosure = {}
  Closure<?> modulesClosure = {}

  List<Module> modules = []

  void handlers(@DelegatesTo(Chain) Closure<?> configurer) {
    this.handlersClosure = configurer
  }

  void modules(@DelegatesTo(ModuleRegistry) Closure<?> configurer) {
    this.modulesClosure = configurer
  }

  @Override
  protected RatpackServer createServer() {
    GuiceBackedHandlerFactory appFactory = createAppFactory()
    def handler = createHandler()
    def modulesAction = createModulesAction()
    Handler appHandler = appFactory.create(modulesAction, handler)

    RatpackServerBuilder builder = new RatpackServerBuilder(appHandler, dir)
    builder.port = 0
    builder.address = null
    builder.build()
  }

  protected GuiceBackedHandlerFactory createAppFactory() {
    new DefaultGuiceBackedHandlerFactory()
  }

  protected Action<? super ModuleRegistry> createModulesAction() {
    action(ModuleRegistry) { ModuleRegistry registry ->
      this.modules.each {
        registry.register(it)
      }
      configure(registry, modulesClosure)
    }
  }

  protected Handler createHandler() {
    chain(action(handlersClosure))
  }

}
