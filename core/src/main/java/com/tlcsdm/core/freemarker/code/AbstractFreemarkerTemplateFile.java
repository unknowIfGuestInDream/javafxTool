/*
 * Copyright (c) 2024 unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.core.freemarker.code;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author unknowIfGuestInDream
 */
public abstract class AbstractFreemarkerTemplateFile implements ITemplateFile {

    private boolean generate = true;
    private boolean directory = false;
    private String filePath;
    protected String generatePath;

    /**
     * Whether is freemarker template (End with .ftl).
     */
    private boolean ftlTemplate;

    /**
     * Template loading location.
     */
    private String templateLoaderPath = "";

    /**
     * The path of the template relative to the loader.
     */
    private String relativePath = "";

    @Override
    public Path getTemplatePath() {
        return Paths.get(templateLoaderPath, relativePath);
    }

    @Override
    public Path getGeneratePath() {
        return Paths.get(generatePath, getFilePath());
    }

    public void setGeneratePath(String generatePath) {
        this.generatePath = generatePath;
    }

    @Override
    public String getFileName() {
        return Paths.get(getFilePath()).getFileName().toString();
    }

    public boolean getFtl() {
        return relativePath.endsWith(".ftl");
    }

    public String getFilePath() {
        if (filePath != null) {
            return filePath;
        }
        if (relativePath.endsWith(".ftl")) {
            return relativePath.substring(0, relativePath.length() - 4);
        }
        return relativePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean getGenerate() {
        return generate;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public boolean getDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public boolean getFtlTemplate() {
        return ftlTemplate;
    }

    public void setFtlTemplate(boolean ftlTemplate) {
        this.ftlTemplate = ftlTemplate;
    }

    public String getTemplateLoaderPath() {
        return templateLoaderPath;
    }

    public void setTemplateLoaderPath(String templateLoaderPath) {
        this.templateLoaderPath = templateLoaderPath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
