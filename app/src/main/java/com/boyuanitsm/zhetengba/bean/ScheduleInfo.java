package com.boyuanitsm.zhetengba.bean;

import java.util.Date;

/**
 * 档期
 * Created by xiaoke on 2016/6/1.
 */
public class ScheduleInfo {
    private String	scheduleId;//档期ID',
    private String	userId;//用户ID',
    private String	labelId;//标签ID',
    private String	personId;//创建人
    private String	startTime;//档期开始时间',
    private String	endTime ;//档期结束时间',
    private Integer	 scheduleVisibility;//可见性（默认为1）1全部可见2好友可见',
    private Integer	 followNum ;//关注数',
    private Boolean	 isValid ;//是否有效.0:无效;1:有效',
    private String createTime;//创建时间
    private String createPersonId;//创建人Id；
    private String noticeUserIds;//通知指定谁看
    private String invisibleUserIds;//不让谁看
    private String	modifyPersonId;//修改人',
    private String	modifyTime;//修改时间',
    private String userName;
    private String userIcon;//用户头像
    private String userNm;//用户昵称
    private String userSex;//用户性别
    private String dictName;//标签名称
    public String getNoticeUserIds() {
        return noticeUserIds;
    }

    public void setNoticeUserIds(String noticeUserIds) {
        this.noticeUserIds = noticeUserIds;
    }

    public String getInvisibleUserIds() {
        return invisibleUserIds;
    }

    public void setInvisibleUserIds(String invisibleUserIds) {
        this.invisibleUserIds = invisibleUserIds;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(String createPersonId) {
        this.createPersonId = createPersonId;
    }



    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getErsonId() {
        return personId;
    }

    public void setErsonId(String personId) {
        this.personId = personId;
    }

    public String getTime() {
        return createTime;
    }

    public void setTime(String time) {
        this.createTime = time;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getModifyPersonId() {
        return modifyPersonId;
    }

    public void setModifyPersonId(String modifyPersonId) {
        this.modifyPersonId = modifyPersonId;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getScheduleVisibility() {
        return scheduleVisibility;
    }

    public void setScheduleVisibility(Integer scheduleVisibility) {
        this.scheduleVisibility = scheduleVisibility;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}