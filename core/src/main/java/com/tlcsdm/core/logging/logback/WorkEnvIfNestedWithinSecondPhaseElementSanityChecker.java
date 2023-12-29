/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

package com.tlcsdm.core.logging.logback;

import ch.qos.logback.classic.model.LoggerModel;
import ch.qos.logback.classic.model.RootLoggerModel;
import ch.qos.logback.core.joran.sanity.Pair;
import ch.qos.logback.core.joran.sanity.SanityChecker;
import ch.qos.logback.core.model.AppenderModel;
import ch.qos.logback.core.model.Model;
import ch.qos.logback.core.spi.ContextAwareBase;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link SanityChecker} to ensure that {@code workEnv} elements are not nested  within second-phase elements.
 *
 * @author unknowIfGuestInDream
 * @see ch.qos.logback.classic.joran.sanity.IfNestedWithinSecondPhaseElementSC
 */
@SuppressWarnings("all")
public class WorkEnvIfNestedWithinSecondPhaseElementSanityChecker extends ContextAwareBase implements SanityChecker {

    private static final List<Class<? extends Model>> SECOND_PHASE_TYPES = List.of(AppenderModel.class,
        LoggerModel.class, RootLoggerModel.class);

    @Override
    public void check(Model model) {
        if (model == null) {
            return;
        }
        List<Model> models = new ArrayList<>();
        SECOND_PHASE_TYPES.forEach((type) -> deepFindAllModelsOfType(type, models, model));
        List<Pair<Model, Model>> nestedPairs = deepFindNestedSubModelsOfType(WorkEnvModel.class, models);
        if (!nestedPairs.isEmpty()) {
            addWarn("<workEnv> elements cannot be nested within an <appender>, <logger> or <root> element");
            nestedPairs.forEach((nested) -> {
                Model first = nested.first;
                Model second = nested.second;
                addWarn("Element <%s> at line %s contains a nested <%s> element at line %s".formatted(first.getTag(),
                    first.getLineNumber(), second.getTag(), second.getLineNumber()));
            });
        }
    }

}
