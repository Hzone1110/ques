<template>
  <div class="login">
    <div
      class="login-wrap"
      style="background: #fff; padding: 20px; border-radius: 10px"
    >
      <h3 class="title">问卷系统-忘记密码</h3>
      <el-tabs v-model="activeName">
          <el-form
            ref="loginFormMobilecode"
            :model="loginFormMobile"
            :rules="rules"
            class="login-form"
          >
            <el-form-item prop="mobile">
              <el-input
                v-model="loginFormMobile.mobile"
                size="small"
                auto-complete="off"
                placeholder="请输入手机号码"
              >
                <svg-icon
                  slot="prefix"
                  icon-class="user"
                  class="el-input__icon input-icon"
                />
              </el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input
                v-model="loginFormMobile.code"
                auto-complete="off"
                placeholder="验证码"
                style="width: 100%"
                @keyup.enter.native="handleReset"
              >
                <svg-icon
                  slot="prefix"
                  icon-class="validCode"
                  class="el-input__icon input-icon hahaCode"
                />
                <template slot="append">
                  <span
                    :class="[{ display: msgKey }]"
                    class="msg-text"
                    @click="handleSend"
                    >{{ msgText }}</span
                  >
                </template>
              </el-input>
            </el-form-item>
             <el-form-item prop="password">
              <el-input
                v-model="loginFormMobile.password"
                type="password"
                auto-complete="off"
                placeholder="输入新密码"
                @keyup.enter.native="handleReset"
              >
                <svg-icon
                  slot="prefix"
                  icon-class="password"
                  class="el-input__icon input-icon"
                />
              </el-input>
            </el-form-item>
             <span class="regc" @click="toLogin">去登录</span>
            <el-form-item style="width: 100%">
              <el-button
                :loading="loading"
                size="medium"
                type="primary"
                style="width: 100%"
                @click.native.prevent="handleReset"
              >
                <span v-if="!loading">重置密码</span>
                <span v-else>重置密码中...</span>
              </el-button>
            </el-form-item>
          </el-form>
      </el-tabs>
    </div>

    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2022 Penint All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { getResetCode,resetPwd} from "@/api/login";
const MSGINIT = "发送验证码";
// const MSGERROR = '验证码发送失败'
const MSGSCUCCESS = "${time}秒后重发";
const MSGTIME = 60;
export default {
  name: "Login",
  data() {
    return {
      loginFormMobile: {
        mobile: "",
        code: "",
        password: ""
      },

      rules: {
         mobile: [
          { required: true, trigger: "blur", message: "用户名不能为空" },
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" },
        ],
        code: [
          { required: true, trigger: "change", message: "验证码不能为空" },
        ],
      },
      loading: false,
      redirect: undefined,
      activeName: "user",
      msgKey: false,
      msgText: MSGINIT,
      msgTime: MSGTIME,
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
  },
  created() {
  },
  methods: {

    toLogin() {
      this.$router.push("/login");
    },

    isvalidatemobile(mobile) {
      var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (!myreg.test(mobile)) {
        return false;
      } else {
        return true;
      }
    },
    handleReset(){
        this.$refs.loginFormMobilecode.validate((valid) => {
        if (valid) {
          resetPwd(this.loginFormMobile).then((res) => {
            if (res.code == 200) {
              this.msgSuccess(
                "重置密码成功，即将跳转登录页！"
              );
              setTimeout(() => {
                this.toLogin();
              }, 2000);
            }
          });
        }
      });
    },
    handleSend() {
      this.rules = {
        mobile: [
          { required: true, trigger: "blur", message: "手机号不能为空" },
        ],
      };

      this.$refs.loginFormMobilecode.validate((valid) => {
        if (valid) {
          let res = this.isvalidatemobile(this.loginFormMobile.mobile);
          if (res) {
            getResetCode(this.loginFormMobile.mobile).then((res) => {
              console.log(res);
              if (res.code == 200) {
                if (this.msgKey) return;
                this.msgText = MSGSCUCCESS.replace("${time}", this.msgTime);
                this.msgKey = true;
                const time = setInterval(() => {
                  this.msgTime--;
                  this.msgText = MSGSCUCCESS.replace("${time}", this.msgTime);
                  if (this.msgTime === 0) {
                    this.msgTime = MSGTIME;
                    this.msgText = MSGINIT;
                    this.msgKey = false;
                    clearInterval(time);
                  }
                }, 1000);
              }
            });
          }
        }
      });
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/image/login-background2.png");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
  font-size: 25px;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-form .hahaCode {
  height: 39px !important;
  width: 14px !important;
  margin-left: 2px !important;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.msg-text {
  cursor: pointer;
}
.regc {
  height: 25px;
  line-height: 25px;
  font-size: 14px;
  cursor: pointer;
  color: #005980;
}
.regc:hover {
  color: #009fda;
  text-decoration: none;
}
</style>
