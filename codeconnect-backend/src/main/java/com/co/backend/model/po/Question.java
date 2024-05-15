package com.co.backend.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author co
 * @since 2024-05-10
 */
@Data
@TableName("question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 题目名称
     */
    private String title;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 题目背景
     */
    private String background;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 输入描述
     */
    private String input;

    /**
     * 输出描述
     */
    private String output;

    /**
     * 题面样例
     */
    private String examples;

    /**
     * 题目难度(0EASY，1MEDIUM，2HARD)
     */
    private Integer difficulty;

    /**
     * 备注、提醒
     */
    private String hint;

    /**
     * 题目模式(0是OI，1是ACM)
     */
    private Integer type;

    /**
     * 权限（0PUBLIC、1PRIVATE）
     */
    private Integer auth;

    /**
     * 判题模式（普通default、特判spj）
     */
    private String judgeMode;

    /**
     * 是否是组内私密题目
     */
    private Boolean isGroup;

    /**
     * 组队id
     */
    private Long gid;

    /**
     * 题目提交数
     */
    private Long submitNum;

    /**
     * 题目通过数
     */
    private Long acceptNum;

    /**
     * 时间限制（单位ms）
     */
    private Integer timeLimit;

    /**
     * 空间限制（单位kb）
     */
    private Integer memoryLimit;

    /**
     * 堆栈限制（单位mb）
     */
    private Integer stackLimit;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除(0否，1删)
     */
    private Boolean isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }
    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }
    public String getJudgeMode() {
        return judgeMode;
    }

    public void setJudgeMode(String judgeMode) {
        this.judgeMode = judgeMode;
    }
    public Boolean getGroup() {
        return isGroup;
    }

    public void setGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }
    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }
    public Long getSubmitNum() {
        return submitNum;
    }

    public void setSubmitNum(Long submitNum) {
        this.submitNum = submitNum;
    }
    public Long getAcceptNum() {
        return acceptNum;
    }

    public void setAcceptNum(Long acceptNum) {
        this.acceptNum = acceptNum;
    }
    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }
    public Integer getStackLimit() {
        return stackLimit;
    }

    public void setStackLimit(Integer stackLimit) {
        this.stackLimit = stackLimit;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title=" + title +
            ", uid=" + uid +
            ", background=" + background +
            ", description=" + description +
            ", input=" + input +
            ", output=" + output +
            ", examples=" + examples +
            ", difficulty=" + difficulty +
            ", hint=" + hint +
            ", type=" + type +
            ", auth=" + auth +
            ", judgeMode=" + judgeMode +
            ", isGroup=" + isGroup +
            ", gid=" + gid +
            ", submitNum=" + submitNum +
            ", acceptNum=" + acceptNum +
            ", timeLimit=" + timeLimit +
            ", memoryLimit=" + memoryLimit +
            ", stackLimit=" + stackLimit +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", isDelete=" + isDelete +
        "}";
    }
}
