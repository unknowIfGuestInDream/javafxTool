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

package com.tlcsdm.core.dsl.commands;

/**
 * @author Konloch
 * @since Jan, 17th, 2017
 */
public class DSLDefinedCommand {
    private final DSLCommandType type;
    private final String name;
    private VariableRunnable variableRunnable;
    private FunctionRunnable functionRunnable;

    /**
     * Creates a new DSLDefinedCommand and defines it as a variable.
     *
     * @param name             any String as the variable name
     * @param variableRunnable any VariableRunnable
     */
    public DSLDefinedCommand(String name, VariableRunnable variableRunnable) {
        type = DSLCommandType.VARIABLE;
        this.name = name;
        this.variableRunnable = variableRunnable;
    }

    /**
     * Constructs a new DSLDefinedCommand and defines it as a function.
     *
     * @param name             any String as the variable name
     * @param functionRunnable any FunctionalRunnable
     */
    public DSLDefinedCommand(String name, FunctionRunnable functionRunnable) {
        type = DSLCommandType.FUNCTION;
        this.name = name;
        this.functionRunnable = functionRunnable;
    }

    /**
     * Returns the type
     *
     * @return the defined DSLCommandType type
     */
    public DSLCommandType getType() {
        return type;
    }

    /**
     * Returns the name
     *
     * @return the defined String name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the VariableRunnable if it's defined, or it will return null.
     *
     * @return returns the VariableRunnable if it's defined, or it will return null
     */
    public VariableRunnable getVariableRunnable() {
        return variableRunnable;
    }

    /**
     * Returns the FunctionRunnable if it's defined, or it will return null
     *
     * @return returns the FunctionRunnable if it's defined, or it will return null
     */
    public FunctionRunnable getFunctionRunnable() {
        return functionRunnable;
    }
}
