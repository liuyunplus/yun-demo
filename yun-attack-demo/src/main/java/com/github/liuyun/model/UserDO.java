package com.github.liuyun.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class UserDO {

    /** 用户账号 */
    private String username;
    /** 密码 */
    private String password;
    /** 用户昵称 */
    private String nickname;
    /** 用户头像 */
    private String avatar;
    /** 用户性别 0-未知, 1-男, 2-女 */
    private Integer gender;
    /** 手机号 */
    private String mobile;
    /** 邮箱 */
    private String email;

}
