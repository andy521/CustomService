package com.ksyun.customservice.object;

/**
 * Created by Administrator on 2015/12/11.
 */
public class UserInfoUtil {
   public static UserInfoUtil userInfoUtil;
    private String id,email,name,mobile,company_name;

    private UserInfoUtil() {
    }

    public static UserInfoUtil getInstance() {
        if (userInfoUtil == null) {
            synchronized (UserInfoUtil.class) {
                userInfoUtil = new UserInfoUtil();
            }
        }
        return userInfoUtil;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
