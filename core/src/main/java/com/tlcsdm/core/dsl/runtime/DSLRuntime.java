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

import com.tlcsdm.core.dsl.DSL;
import com.tlcsdm.core.dsl.commands.DSLCommandType;
import com.tlcsdm.core.dsl.commands.DSLDefinedCommand;
import com.tlcsdm.core.util.SplitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * The DSLRuntime parses and executes Strings based on the supplied DSL.
 *
 * @author Konloch
 * @since Jan, 17th, 2017
 */
public class DSLRuntime {
    private final DSL dsl;
    private final LinkedHashMap<String, DSLRuntimeCommand> commands = new LinkedHashMap<>();
    private String insideSubscript;

    /**
     * Construct a new instance of the DSLRuntime
     *
     * @param dsl any DSL
     */
    public DSLRuntime(DSL dsl) {
        this.dsl = dsl;
    }

    /**
     * Signal to the runtime that the parsing has stopped externally.
     */
    public void stopParse() {
        insideSubscript = null;
    }

    /**
     * Attempts to parse any String and runs it if it's part of the init, or else it will store the DLSRuntimeCommand under the read subscript
     *
     * @param line any String
     */
    public void parseLine(String line) {
        if (line == null)
            return;

        line = line.trim();

        if (line.isEmpty())
            return;

        if (line.startsWith(dsl.getCommentDelimiter()))
            return;

        //if not currently processing a subscript, parse normally
        if (insideSubscript == null) {
            if (line.contains(dsl.getBracketDelimiterStart())) {
                //turn the line into a runtime command
                DSLRuntimeCommand command = buildRuntimeCommand(line);

                //TODO should alert unknown command was reached here
                if (command == null)
                    return;

                //store the parsed DSL command to be processed if needed
                commands.put(command.getName(), command);

                //execute the runtime command
                execute(command);
            } else if (line.endsWith(dsl.getSubscriptDelimiterStart())) {
                String functionName = line.substring(0, line.length() - 1).trim();

                if (!functionName.isEmpty() && (!dsl.isStrictMode() || dsl.isStrictMode() && dsl.getSubscripts().containsKey(functionName))) {
                    insideSubscript = functionName;
                }
            } else {
                //turn the line into a runtime command
                DSLRuntimeCommand command = buildRuntimeCommand(line);

                //TODO should alert unknown command was reached here
                if (command == null)
                    return;

                //store the parsed DSL command to be processed if needed
                commands.put(command.getName(), command);

                //execute the runtime command
                execute(command);
            }
        } else {
            if (line.contains(dsl.getSubscriptDelimiterEnd())) {
                insideSubscript = null;
            } else {
                //get the subscript list or create one if it doesn't exist
                ArrayList<DSLRuntimeCommand> sub;
                if (dsl.getSubscripts().containsKey(insideSubscript))
                    sub = (ArrayList<DSLRuntimeCommand>) dsl.getSubscripts().get(insideSubscript);
                else {
                    sub = new ArrayList<>();
                    dsl.getSubscripts().put(insideSubscript, sub);
                }

                //turn the line into a runtime command
                DSLRuntimeCommand command = buildRuntimeCommand(line);

                //add the lines to the script
                sub.add(command);
            }
        }
    }

    /**
     * Executes a specific DLSRuntimeCommand
     *
     * @param runtimeCommand any DLSRuntimeCommand
     */
    public void execute(DSLRuntimeCommand runtimeCommand) {
        DSLDefinedCommand command = dsl.getCommands().get(runtimeCommand.getName());

        if (command == null) {
            //TODO if verbose or alternative if strict throw new RuntimeException
            //System.out.println("No variable or function named `" + runtimeCommand.getName() + "` has been defined.");
            return;
        }

        switch (command.getType()) {
            case VARIABLE:
                command.getVariableRunnable().run(runtimeCommand.getVariableValue(this));
                break;

            case FUNCTION:
                command.getFunctionRunnable().run(runtimeCommand.getParameters());
                break;
        }
    }

    /**
     * Attempts to build a DSLRuntimeCommand from any String.
     *
     * @param line any String
     * @return the DSLRuntimeCommand if it can be created, if not it will return null
     */
    public DSLRuntimeCommand buildRuntimeCommand(String line) {
        //does an unrestricted quick then first, then a deeper verification before it creates the variable
        if (line.contains(dsl.getSetValueDelimiter())) {
            String[] split = SplitUtil.split(line, dsl.getSetValueDelimiter(), 2);
            String name = split[0].trim();
            String value = split[1].trim();

            //verify the data is valid, then make sure the runtime command has a handler
            //if it does, assume this is a variable
            //TODO if strict true, it should re-enable the runtime command handler check
            if (!name.isEmpty() && !value.isEmpty() && (!dsl.isStrictMode() || dsl.isStrictMode() && dsl.getCommands().containsKey(name)))
                return new DSLRuntimeCommand(DSLCommandType.VARIABLE, name, new String[]{value});
        }

        //look for the function bracket delimiters
        if (line.contains(dsl.getBracketDelimiterStart()) && line.contains(dsl.getBracketDelimiterEnd())) {
            String[] parameters = SplitUtil.split(line, dsl.getBracketDelimiterStart(), 2);
            String name = parameters[0].trim();
            String values = null;

            if (parameters[1].length() >= 2) {
                values = SplitUtil.split(parameters[1], dsl.getBracketDelimiterEnd())[0].trim();
            }

            if (values != null) {
                if (values.contains(",")) {
                    parameters = SplitUtil.split(values, ",");

                    //this allows the user to have spaces between the function separator.
                    //one downside to doing it this way, is users lose the ability to have trailing whitespace.
                    //this limitation is part of the specification along with the hardcoded `,` delimiter.
                    //these should be looked at being changed at a later date if needed.
                    for (int i = 0; i < parameters.length; i++)
                        parameters[i] = parameters[i].trim();
                } else {
                    parameters = new String[]{values};
                }

                return new DSLRuntimeCommand(DSLCommandType.VARIABLE, name, parameters);
            } else {
                return new DSLRuntimeCommand(DSLCommandType.VARIABLE, name, null);
            }
        }

        return null;
    }

    /**
     * Returns all the DLSRuntimeCommands created during runtime.
     *
     * @return the commands in the form of a HashMap
     */
    public HashMap<String, DSLRuntimeCommand> getCommands() {
        return commands;
    }

    /**
     * Returns the DSL linked with this DSLRuntime instance.
     *
     * @return the DSL instance
     */
    public DSL getDSL() {
        return dsl;
    }
}
