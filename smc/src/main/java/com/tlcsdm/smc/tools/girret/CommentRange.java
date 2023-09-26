package com.tlcsdm.smc.tools.girret;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * girret指摘范围.
 * 
 * @author unknowIfGuestInDream
 */
public class CommentRange implements Serializable {

    private static final long serialVersionUID = 5634618213766325512L;

    @JsonProperty("start_line")
    private Integer startLine;
    @JsonProperty("start_character")
    private Integer startCharacter;
    @JsonProperty("end_line")
    private Integer endLine;
    @JsonProperty("end_character")
    private Integer endCharacter;

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getStartCharacter() {
        return startCharacter;
    }

    public void setStartCharacter(Integer startCharacter) {
        this.startCharacter = startCharacter;
    }

    public Integer getEndLine() {
        return endLine;
    }

    public void setEndLine(Integer endLine) {
        this.endLine = endLine;
    }

    public Integer getEndCharacter() {
        return endCharacter;
    }

    public void setEndCharacter(Integer endCharacter) {
        this.endCharacter = endCharacter;
    }

}
