package com.tlcsdm.smc.tools.girret;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * girret提交.
 * 
 * @author unknowIfGuestInDream
 */
public class Change implements Serializable {

    private static final long serialVersionUID = -7742798462773631839L;

    private String id;
    private String project;
    private String branch;
    @JsonProperty("change_id")
    private String changeId;
    private String subject;
    private String status;
    private String created;
    private String updated;
    private String submitted;
    private Integer insertions;
    private Integer deletions;
    @JsonProperty("total_comment_count")
    private Integer totalCommentCount;
    @JsonProperty("_number")
    private Long number;
    private Owner owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public Integer getInsertions() {
        return insertions;
    }

    public void setInsertions(Integer insertions) {
        this.insertions = insertions;
    }

    public Integer getDeletions() {
        return deletions;
    }

    public void setDeletions(Integer deletions) {
        this.deletions = deletions;
    }

    public Integer getTotalCommentCount() {
        return totalCommentCount;
    }

    public void setTotalCommentCount(Integer totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

}
