package com.tlcsdm.core.freemarker.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.tlcsdm.core.exception.UnExpectedResultException;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *  FreeMarker user-defined directive that progressively transforms
 *  the output of its nested content to lower-case.
 *  
 *  <p><b>Directive info</b></p>
 * 
 *  <p>Directive parameters: None
 *  <p>Loop variables: None
 *  <p>Directive nested content: Yes
 */
public class LowerDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        // Check if no parameters were given:
        if (!params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow parameters.");
        }
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }

        // If there is non-empty nested content:
        if (body != null) {
            // Executes the nested body. Same as <#nested> in FTL, except
            // that we use our own writer instead of the current output writer.
            body.render(new LowerCaseFilterWriter(env.getOut()));
        } else {
            throw new UnExpectedResultException("missing body");
        }
    }

    /**
     * A {@link Writer} that transforms the character stream to lower case
     * and forwards it to another {@link Writer}.
     */
    private static class LowerCaseFilterWriter extends Writer {

        private final Writer out;

        LowerCaseFilterWriter(Writer out) {
            this.out = out;
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
            char[] transformedCbuf = new char[len];
            for (int i = 0; i < len; i++) {
                transformedCbuf[i] = Character.toLowerCase(cbuf[i + off]);
            }
            out.write(transformedCbuf);
        }

        public void flush() throws IOException {
            out.flush();
        }

        public void close() throws IOException {
            out.close();
        }
    }

}
