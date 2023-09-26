package com.tlcsdm.smc.tools.girret;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * girret指摘信息.
 * 
 * @author unknowIfGuestInDream
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = 2438636622648638785L;

    private String id;
    private Owner author;
    @JsonProperty("change_message_id")
    private String changeMessageId;
    private Boolean unresolved;
    @JsonProperty("patch_set")
    private Integer patchSet;
    private Integer line;
    private String updated;
    private String message;
    @JsonProperty("commit_id")
    private String commitId;
    private CommentRange range;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Owner getAuthor() {
        return author;
    }

    public void setAuthor(Owner author) {
        this.author = author;
    }

    public String getChangeMessageId() {
        return changeMessageId;
    }

    public void setChangeMessageId(String changeMessageId) {
        this.changeMessageId = changeMessageId;
    }

    public Boolean getUnresolved() {
        return unresolved;
    }

    public void setUnresolved(Boolean unresolved) {
        this.unresolved = unresolved;
    }

    public Integer getPatchSet() {
        return patchSet;
    }

    public void setPatchSet(Integer patchSet) {
        this.patchSet = patchSet;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public CommentRange getRange() {
        return range;
    }

    public void setRange(CommentRange range) {
        this.range = range;
    }

}
