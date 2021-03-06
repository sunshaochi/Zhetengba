package com.boyuanitsm.zhetengba.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangbin on 16/6/23.
 */
public class GroupBean implements Parcelable {


    /**
     * id : d7ae10ca391411e69615eca86ba4ba05
     * groupOwnerId : 7f08d3c932a211e69615eca86ba4ba05
     * type : true
     * activityId : null
     * groupName : 13838396534、小可...
     * createTime : 1466667236000
     * groupMemberCounts : 2
     * timeLength : 1467185635000
     * isValid : true
     * remark : 210893357428244916
     * reminderDays : 7
     */

    private String id;
    private String groupOwnerId;
    private boolean type;//true 为自建群, false 为活动群
    private String activityId;
    private String groupName;
    private String createTime;
    private String groupMemberCounts;
    private String timeLength;
    private String isValid;
    private String remark;
    private String reminderDays;
    private String activitySite;
    private String startTime;
    private String endTime;
    private String inviteNumber;
    private String memberNum;


    public String getActivitySite() {
        return activitySite;
    }

    public void setActivitySite(String activitySite) {
        this.activitySite = activitySite;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInviteNumber() {
        return inviteNumber;
    }

    public void setInviteNumber(String inviteNumber) {
        this.inviteNumber = inviteNumber;
    }

    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
    }

    public static Creator<GroupBean> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupOwnerId() {
        return groupOwnerId;
    }

    public void setGroupOwnerId(String groupOwnerId) {
        this.groupOwnerId = groupOwnerId;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGroupMemberCounts() {
        return groupMemberCounts;
    }

    public void setGroupMemberCounts(String groupMemberCounts) {
        this.groupMemberCounts = groupMemberCounts;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(String reminderDays) {
        this.reminderDays = reminderDays;
    }


    public GroupBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.groupOwnerId);
        dest.writeByte(this.type ? (byte) 1 : (byte) 0);
        dest.writeString(this.activityId);
        dest.writeString(this.groupName);
        dest.writeString(this.createTime);
        dest.writeString(this.groupMemberCounts);
        dest.writeString(this.timeLength);
        dest.writeString(this.isValid);
        dest.writeString(this.remark);
        dest.writeString(this.reminderDays);
        dest.writeString(this.activitySite);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.inviteNumber);
        dest.writeString(this.memberNum);
    }

    protected GroupBean(Parcel in) {
        this.id = in.readString();
        this.groupOwnerId = in.readString();
        this.type = in.readByte() != 0;
        this.activityId = in.readString();
        this.groupName = in.readString();
        this.createTime = in.readString();
        this.groupMemberCounts = in.readString();
        this.timeLength = in.readString();
        this.isValid = in.readString();
        this.remark = in.readString();
        this.reminderDays = in.readString();
        this.activitySite = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.inviteNumber = in.readString();
        this.memberNum = in.readString();
    }

    public static final Creator<GroupBean> CREATOR = new Creator<GroupBean>() {
        @Override
        public GroupBean createFromParcel(Parcel source) {
            return new GroupBean(source);
        }

        @Override
        public GroupBean[] newArray(int size) {
            return new GroupBean[size];
        }
    };
}
