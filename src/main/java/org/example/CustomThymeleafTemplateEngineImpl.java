package org.example;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.common.WebEnvironment;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.linkbuilder.StandardLinkBuilder;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import java.io.Writer;
import java.nio.charset.Charset;
import java.util.*;

public class CustomThymeleafTemplateEngineImpl implements ThymeleafTemplateEngine {
    private final TemplateEngine templateEngine = new TemplateEngine();
    private final ResourceTemplateResolver templateResolver;

    public CustomThymeleafTemplateEngineImpl(Vertx vertx) {
        ResourceTemplateResolver templateResolver = new ResourceTemplateResolver(vertx);
        templateResolver.setCacheable(!WebEnvironment.development());
        templateResolver.setTemplateMode(ThymeleafTemplateEngine.DEFAULT_TEMPLATE_MODE);
        this.templateResolver = templateResolver;
        this.templateEngine.setTemplateResolver(templateResolver);
        this.templateEngine.setLinkBuilder(new StandardLinkBuilder() {
            protected String computeContextPath(IExpressionContext context, String base, Map<String, Object> parameters) {
                return "/";
            }
        });
    }

    public <T> T unwrap() {
        return (T) this.templateEngine;
    }

    public void clearCache() {
        this.templateEngine.clearTemplateCache();
    }

    public ThymeleafTemplateEngine setMode(TemplateMode mode) {
        this.templateResolver.setTemplateMode(mode);
        return this;
    }

    public TemplateEngine getThymeleafTemplateEngine() {
        return this.templateEngine;
    }

    public void render(Map<String, Object> context, String templateFile, Handler<AsyncResult<Buffer>> handler) {
        final Buffer buffer = Buffer.buffer();

        Set<String> templateSelectors = new HashSet<>();

        if (templateFile.contains("::")) {

            String[] strings = templateFile.split("::");

            templateFile = strings[0].concat(".html");

            for (String selector : Arrays.copyOfRange(strings, 1, strings.length)) {
                templateSelectors.add(selector);
            }

        }

        try {
            synchronized(this) {
                this.templateEngine.process(templateFile, templateSelectors, new WebIContext(context, (String)context.get("lang")), new Writer() {
                    public void write(char[] cbuf, int off, int len) {
                        buffer.appendString(new String(cbuf, off, len));
                    }

                    public void flush() {
                    }

                    public void close() {
                    }
                });
            }

            handler.handle(Future.succeededFuture(buffer));
        } catch (Exception var8) {
            Exception ex = var8;
            handler.handle(Future.failedFuture(ex));
        }

    }

    @Override
    public boolean isCachingEnabled() {
        return false;
    }

    private static class ResourceTemplateResolver extends StringTemplateResolver {
        private final Vertx vertx;

        ResourceTemplateResolver(Vertx vertx) {
            this.vertx = vertx;
            this.setName("vertx/Thymeleaf3");
        }

        protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
            return new StringTemplateResource(this.vertx.fileSystem().readFileBlocking(template).toString(Charset.defaultCharset()));
        }
    }

    private static class WebIContext implements IContext {
        private final Map<String, Object> data;
        private final Locale locale;

        private WebIContext(Map<String, Object> data, String lang) {
            this.data = data;
            if (lang == null) {
                this.locale = Locale.getDefault();
            } else {
                this.locale = Locale.forLanguageTag(lang);
            }

        }

        public Locale getLocale() {
            return this.locale;
        }

        public boolean containsVariable(String name) {
            return this.data.containsKey(name);
        }

        public Set<String> getVariableNames() {
            return this.data.keySet();
        }

        public Object getVariable(String name) {
            return this.data.get(name);
        }
    }
}
