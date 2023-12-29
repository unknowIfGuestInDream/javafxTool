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

import ch.qos.logback.core.Context;
import ch.qos.logback.core.model.Model;
import ch.qos.logback.core.model.processor.ModelHandlerBase;
import ch.qos.logback.core.model.processor.ModelHandlerException;
import ch.qos.logback.core.model.processor.ModelInterpretationContext;
import com.tlcsdm.core.util.CoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Logback {@link ModelHandlerBase model handler} to support {@code <workEnv>} tags.
 * Allows Logback properties to be sourced from the Spring environment.
 *
 * @author unknowIfGuestInDream
 * @see WorkEnvAction
 * @see WorkEnvModel
 */
public class WorkEnvModelHandler extends ModelHandlerBase {

    WorkEnvModelHandler(Context context) {
        super(context);
    }

    @Override
    public void handle(ModelInterpretationContext intercon, Model model) throws ModelHandlerException {
        WorkEnvModel profileModel = (WorkEnvModel) model;
        if (!acceptsProfiles(intercon, profileModel)) {
            model.deepMarkAsSkipped();
        }
    }

    private boolean acceptsProfiles(ModelInterpretationContext ic, WorkEnvModel model) {
        String[] profileNames = model.getName().split(",");
        List<String> list = new ArrayList<>(profileNames.length);
        for (String profile : profileNames) {
            list.add(profile.trim());
        }
        if (list.isEmpty()) {
            return false;
        }
        return list.contains(CoreUtil.getWorkEnv());
    }

}
