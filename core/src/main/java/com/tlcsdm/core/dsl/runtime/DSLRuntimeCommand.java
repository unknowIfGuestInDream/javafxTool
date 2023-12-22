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

package com.tlcsdm.core.dsl.runtime;

import com.tlcsdm.core.dsl.commands.DSLCommandType;
import com.tlcsdm.core.dsl.stringvars.StringVars;

/**
 * A DSLCommand represents a variable or a function.
 *
 * @author Konloch
 * @since Jan, 17th, 2017
 */
public class DSLRuntimeCommand {
    private final DSLCommandType DSLCommandType;
    private final String name;
    private final String[] parameters;

    /**
     * Constructs a new DSLRuntimeCommand
     *
     * @param DSLCommandType the DSLCommandType type
     * @param name           any String as the name
     * @param parameters     any String Array as the parameters
     */
    public DSLRuntimeCommand(DSLCommandType DSLCommandType, String name, String[] parameters) {
        this.DSLCommandType = DSLCommandType;
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * Return the DSLCommandType type
     *
     * @return the defined DLSCommandType type
     */
    public DSLCommandType getType() {
        return DSLCommandType;
    }

    /**
     * Return the defined name
     *
     * @return the defined String name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the defined parameters
     *
     * @return the defined String Array parameters
     */
    public String[] getParameters() {
        return parameters;
    }

    /**
     * Preforms a recursive search if the variable value contains a variable delimiter / variable name
     *
     * @param runtime the DSLRuntime instance
     * @return the fully extracted String variable
     */
    public String getVariableValue(DSLRuntime runtime) {
        return StringVars.getVariableValue(runtime.getDSL().getVariableDelimiter(),
            () -> getParameters()[0], (key) -> runtime.getCommands().get(key).getVariableValue(runtime));
    }
}
