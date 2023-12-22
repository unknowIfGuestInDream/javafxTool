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

package com.tlcsdm.core.dsl;

import cn.hutool.core.io.FileUtil;
import com.tlcsdm.core.dsl.commands.DSLCommandType;
import com.tlcsdm.core.dsl.commands.DSLDefinedCommand;
import com.tlcsdm.core.dsl.commands.FunctionRunnable;
import com.tlcsdm.core.dsl.commands.VariableRunnable;
import com.tlcsdm.core.dsl.runtime.DSLRuntime;
import com.tlcsdm.core.dsl.runtime.DSLRuntimeCommand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the data portion of the DSL.
 * <p>
 * This class is used to configure and start the DSL Runtime.
 *
 * @author Konloch
 * @since Jan, 17th, 2017
 */
public class DSL {
    private final String setValueDelimiter;
    private final char variableDelimiter;
    private final String bracketDelimiterStart;
    private final String bracketDelimiterEnd;
    private final String subscriptDelimiterStart;
    private final String subscriptDelimiterEnd;
    private final String commentDelimiter;
    private final boolean strictMode;
    private final HashMap<String, DSLDefinedCommand> commands = new HashMap<>();
    private final HashMap<String, List<DSLRuntimeCommand>> subscripts = new HashMap<>();
    private final DSLRuntime runtime = new DSLRuntime(this);

    /**
     * Constructs a new DSL instance with strict mode disabled by default.
     *
     * @param setValueDelimiter       any character to represent the variable value delimiter
     * @param variableDelimiter       any character to represent the variable delimiter
     * @param bracketDelimiterStart   any character to represent the bracket delimiter start
     * @param bracketDelimiterEnd     any character to represent the bracket delimiter end
     * @param subscriptDelimiterStart any character to represent the subscript delimiter start
     * @param subscriptDelimiterEnd   any character to represent the subscript delimiter end
     * @param commentDelimiter        any character to represent the comment delimiter
     */
    public DSL(char setValueDelimiter, char variableDelimiter,
               char bracketDelimiterStart, char bracketDelimiterEnd,
               char subscriptDelimiterStart, char subscriptDelimiterEnd,
               char commentDelimiter) {
        this(setValueDelimiter, variableDelimiter,
            bracketDelimiterStart, bracketDelimiterEnd,
            subscriptDelimiterStart, subscriptDelimiterEnd,
            commentDelimiter, false);
    }

    /**
     * Constructs a new DSL instance.
     *
     * @param setValueDelimiter       any character to represent the variable value delimiter
     * @param variableDelimiter       any character to represent the variable delimiter
     * @param bracketDelimiterStart   any character to represent the bracket delimiter start
     * @param bracketDelimiterEnd     any character to represent the bracket delimiter end
     * @param subscriptDelimiterStart any character to represent the subscript delimiter start
     * @param subscriptDelimiterEnd   any character to represent the subscript delimiter end
     * @param commentDelimiter        any character to represent the comment delimiter
     * @param strictMode              true for strict mode to be enabled
     */
    public DSL(char setValueDelimiter, char variableDelimiter,
               char bracketDelimiterStart, char bracketDelimiterEnd,
               char subscriptDelimiterStart, char subscriptDelimiterEnd,
               char commentDelimiter, boolean strictMode) {
        this.setValueDelimiter = String.valueOf(setValueDelimiter);
        this.variableDelimiter = variableDelimiter;
        this.bracketDelimiterStart = String.valueOf(bracketDelimiterStart);
        this.bracketDelimiterEnd = String.valueOf(bracketDelimiterEnd);
        this.subscriptDelimiterStart = String.valueOf(subscriptDelimiterStart);
        this.subscriptDelimiterEnd = String.valueOf(subscriptDelimiterEnd);
        this.commentDelimiter = String.valueOf(commentDelimiter);
        this.strictMode = strictMode;
    }

    /**
     * Clear all the user defined commands and subscripts. It also removes all runtime data.
     *
     * @return this instance for method chaining
     */
    public DSL clear() {
        //clear the runtime
        runtime.getCommands().clear();
        runtime.stopParse();

        //clear the user defined data
        commands.clear();

        //clear the subscripts
        subscripts.clear();
        return this;
    }

    /**
     * Parse any File and execute / load the script.
     *
     * @param file any file
     * @return this instance for method chaining
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public DSL parse(File file) throws IOException {
        ArrayList<String> list = (ArrayList<String>) FileUtil.readUtf8Lines(file);
        parse(list);
        return this;
    }

    /**
     * Parse any String ArrayList and execute / load the script.
     *
     * @param fileContents any String ArrayList
     * @return this instance for method chaining
     */
    public DSL parse(ArrayList<String> fileContents) {
        for (String line : fileContents)
            runtime.parseLine(line);

        runtime.stopParse();
        return this;
    }

    /**
     * Add a new variable handler.
     *
     * @param name             any String as the variable name
     * @param variableRunnable any VariableRunnable to be called when the variable set value gets called
     * @return this instance for method chaining
     */
    public DSL addVar(String name, VariableRunnable variableRunnable) {
        commands.put(name, new DSLDefinedCommand(name, variableRunnable));
        return this;
    }

    /**
     * Remove a variable handler.
     *
     * @param name any String as the variable name
     * @return this instance for method chaining
     */
    public DSL removeVar(String name) {
        DSLDefinedCommand command = commands.get(name);
        if (command != null && command.getType() == DSLCommandType.VARIABLE)
            commands.remove(name);

        return this;
    }

    /**
     * Add a new function handler.
     *
     * @param name             any String as the function name
     * @param functionRunnable any FunctionRunnable to be called when the function gets called
     * @return this instance for method chaining
     */
    public DSL addFunc(String name, FunctionRunnable functionRunnable) {
        commands.put(name, new DSLDefinedCommand(name, functionRunnable));
        return this;
    }

    /**
     * Remove a function handler.
     *
     * @param name any String as the function name
     * @return this instance for method chaining
     */
    public DSL removeFunc(String name) {
        DSLDefinedCommand command = commands.get(name);
        if (command != null && command.getType() == DSLCommandType.FUNCTION)
            commands.remove(name);

        return this;
    }

    /**
     * Define a subscript.
     *
     * @param name any String as the subscript name
     * @return this instance for method chaining
     */
    public DSL addSub(String name) {
        subscripts.put(name, new ArrayList<>());
        return this;
    }

    /**
     * Remove a subscript.
     *
     * @param name any String as the subscript name
     * @return this instance for method chaining
     */
    public DSL removeSub(String name) {
        subscripts.remove(name);
        return this;
    }

    /**
     * Runs a subscript associated with a String name. Throws a Runtime Exception if the Subscript doesn't exist.
     *
     * @param name any String as the subscript name
     * @return this instance for method chaining
     */
    public DSL run(String name) {
        List<DSLRuntimeCommand> functionContents = subscripts.get(name);

        if (functionContents == null)
            throw new RuntimeException("Subscript " + name + " not found");

        for (DSLRuntimeCommand command : functionContents)
            runtime.execute(command);

        return this;
    }

    /**
     * Returns the set value delimiter
     *
     * @return the set value delimiter as a String
     */
    public String getSetValueDelimiter() {
        return setValueDelimiter;
    }

    /**
     * Returns the bracket delimiter start
     *
     * @return the bracket delimiter start as a String
     */
    public String getBracketDelimiterStart() {
        return bracketDelimiterStart;
    }

    /**
     * Returns the bracket delimiter end
     *
     * @return the bracket delimiter end as a String
     */
    public String getBracketDelimiterEnd() {
        return bracketDelimiterEnd;
    }

    /**
     * Returns the subscript delimiter start
     *
     * @return the subscript delimiter start as a String
     */
    public String getSubscriptDelimiterStart() {
        return subscriptDelimiterStart;
    }

    /**
     * Returns the subscript delimiter end
     *
     * @return the subscript delimiter end as a String
     */
    public String getSubscriptDelimiterEnd() {
        return subscriptDelimiterEnd;
    }

    /**
     * Returns the variable delimiter
     *
     * @return the variable delimiter as a String
     */
    public char getVariableDelimiter() {
        return variableDelimiter;
    }

    /**
     * Returns the comment delimiter
     *
     * @return the comment delimiter as a String
     */
    public String getCommentDelimiter() {
        return commentDelimiter;
    }

    /**
     * Returns the strict mode flag
     *
     * @return if true strict mode will be enabled during runtime parsing
     */
    public boolean isStrictMode() {
        return strictMode;
    }

    /**
     * Returns the command map
     *
     * @return the command map as a HashMap
     */
    public HashMap<String, DSLDefinedCommand> getCommands() {
        return commands;
    }

    /**
     * The subscript map
     *
     * @return the subscript map as a HashMap
     */
    public HashMap<String, List<DSLRuntimeCommand>> getSubscripts() {
        return subscripts;
    }

    /**
     * The DLSRuntime associated with this DSL.
     *
     * @return the DSLRuntime for this DSL instance
     */
    public DSLRuntime getRuntime() {
        return runtime;
    }

    /**
     * Alert that this is a library
     *
     * @param args program launch arguments
     */
    public static void main(String[] args) {
        throw new RuntimeException("Incorrect usage - for information on how to use this correctly visit https://konloch.com/DSLBuilder/");
    }
}
