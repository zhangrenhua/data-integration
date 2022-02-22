package com.youngdatafan.portal.system.management.user.vo;

import com.datafan.dataintegration.core.validation.FieldMatch;
import com.youngdatafan.portal.system.management.util.ValidatorConstans;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 接口输入参数
 *
 * @author gavin
 * @create 2020-01-09 16:44
 */

@ApiModel(description = "用户对象")
public class UserAddVO implements Serializable {

    @ApiModelProperty(value = "用户名", required = true)
    @Length(min = 1, max = 30)
    @NotBlank
    private String userName;


    /**
     * 用户描述
     */
    @ApiModelProperty("用户描述")
    private String describe;

    /**
     * 用户手机号
     */
    @ApiModelProperty("用户手机号")
    @Pattern(regexp = ValidatorConstans.PHONE_NUMBER, message = "{user.phone.incorrect}")
    private String userMobile;

    /**
     * 用户邮箱地址
     */
    @ApiModelProperty("用户邮箱地址")
    @Pattern(regexp = ValidatorConstans.EMAIL, message = "{user.email.incorrect}")
    private String userEmail;


    @ApiModelProperty("状态")
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}