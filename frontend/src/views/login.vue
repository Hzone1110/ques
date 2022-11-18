<template>
  <div class="login">
    <div
      class="login-wrap"
      style="background: #fff; padding: 20px; border-radius: 10px"
    >
      <h3 class="title">问卷系统</h3>
      <el-tabs v-model="activeName">
        <el-tab-pane label="用户密码" name="user">
          <el-form
            ref="loginForm"
            :model="loginForm"
            :rules="loginRules"
            class="login-form"
          >
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                type="text"
                auto-complete="off"
                placeholder="账号"
              >
                <svg-icon
                  slot="prefix"
                  icon-class="user"
                  class="el-input__icon input-icon"
                />
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                auto-complete="off"
                placeholder="密码"
                @keyup.enter.native="handleLogin"
              >
                <svg-icon
                  slot="prefix"
                  icon-class="password"
                  class="el-input__icon input-icon"
                />
              </el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input
                v-model="loginForm.code"
                auto-complete="off"
                placeholder="验证码"
                style="width: 63%"
                @keyup.enter.native="handleLogin"
              >
                <svg-icon
                  slot="prefix"
                  icon-class="validCode"
                  class="el-input__icon input-icon"
                />
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" />
              </div>
            </el-form-item>
            <el-checkbox
              v-if="this.activeName == 'user'"
              v-model="loginForm.rememberMe"
              style="margin: 0px 0px 25px 0px"
              >记住密码</el-checkbox
            >

            <span class="regc" @click="toRegisterAnswer">答者注册</span>
            <span class="regc" @click="toRegisterCompany">租户注册</span>

            <el-form-item style="width: 100%">
              <el-button
                :loading="loading"
                size="medium"
                type="primary"
                style="width: 100%"
                @click.native.prevent="handleLogin"
              >
                <span v-if="!loading">登 录</span>
                <span v-else>登 录 中...</span>
              </el-button>
            </el-form-item>
            <div class="reD">
              <span class="rePwd" @click="rePassword"
                >已有帐号，忘记密码？</span
              >
            </div>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="短信验证码" name="code">
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
                @keyup.enter.native="handleLogin"
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
            <el-form-item style="width: 100%">
              <el-button
                :loading="loading"
                size="medium"
                type="primary"
                style="width: 100%"
                @click.native.prevent="handleLogin"
              >
                <span v-if="!loading">登 录</span>
                <span v-else>登 录 中...</span>
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2022 Penint All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { getMobileCode, getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";
const MSGINIT = "发送验证码";
// const MSGERROR = '验证码发送失败'
const MSGSCUCCESS = "${time}秒后重发";
const MSGTIME = 60;
import { isvalidatemobile } from "@/utils/validate";
export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      cookiePassword: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: "",
      },
      loginFormMobile: {
        mobile: "",
        code: "",
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "用户名不能为空" },
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" },
        ],
        code: [
          { required: true, trigger: "change", message: "验证码不能为空" },
        ],
      },
      rules: {
        // phone: [
        //   //{required: true, trigger: "blur", message: "电话不能为空" },
        //   { required: true, trigger: 'blur', validator: validatePhone }],
        // code: [{ required: true, trigger: 'blur',  message: "验证码不能为空" }]
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
    this.getCode();
    this.getCookie();
  },
  methods: {
    toRegisterCompany() {
      this.$router.push("/registerCompany");
    },
    toRegisterAnswer() {
      this.$router.push("/registerAnswer");
    },
    rePassword() {
      this.$router.push("/rePassword");
    },
    getCode() {
      getCodeImg().then((res) => {
        this.codeUrl = "data:image/gif;base64," + res.img;
        this.loginForm.uuid = res.uuid;
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get("rememberMe");
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password:
          password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      };
    },
    handleLogin() {
      if (this.activeName == "user") {
        this.$refs.loginForm.validate((valid) => {
          if (valid) {
            this.loading = true;
            if (this.loginForm.rememberMe) {
              Cookies.set("username", this.loginForm.username, { expires: 30 });
              Cookies.set("password", encrypt(this.loginForm.password), {
                expires: 30,
              });
              Cookies.set("rememberMe", this.loginForm.rememberMe, {
                expires: 30,
              });
            } else {
              Cookies.remove("username");
              Cookies.remove("password");
              Cookies.remove("rememberMe");
            }
            this.$store
              .dispatch("Login", this.loginForm)
              .then(() => {
                this.$router.push({ path: this.redirect || "/" });
              })
              .catch(() => {
                this.loading = false;
                this.getCode();
              });
          }
        });
      } else if (this.activeName == "code") {
        const validatePhone = (rule, value, callback) => {
          if (isvalidatemobile(value)[0]) {
            callback(new Error(isvalidatemobile(value)[1]));
          } else {
            callback();
          }
        };
        this.rules = {};
        this.rules = {
          mobile: [
            { required: true, trigger: "blur", message: "电话不能为空" },
            { required: true, trigger: "blur", validator: validatePhone },
          ],
          code: [
            { required: true, trigger: "blur", message: "验证码不能为空" },
          ],
        };

        this.$refs.loginFormMobilecode.validate((valid) => {
          if (
            valid &&
            this.loginFormMobile.mobile &&
            this.loginFormMobile.code
          ) {
            this.loading = true;
            this.$store
              .dispatch("Login", this.loginFormMobile)
              .then(() => {
                this.$router.push({ path: this.redirect || "/" });
              })
              .catch(() => {
                this.loading = false;
                // this.getCode();
              });
          }
        });
      }
    },
    isvalidatemobile(mobile) {
      var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (!myreg.test(mobile)) {
        return false;
      } else {
        return true;
      }
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
            getMobileCode(this.loginFormMobile.mobile).then((res) => {
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
  // font-size: 14px;
  float: right;
  margin-left: 10px;
  // font-weight: 700;
  font-size: 14px;
  text-align: center;
  cursor: pointer;
  color: #005980;
}
.regc:hover {
  color: #009fda;
  text-decoration: none;
}
.reD {
  text-align: center;
}
.rePwd {
  font-size: 14px;
  text-align: center;
  cursor: pointer;
  color: #005980;
}
.rePwd:hover {
  color: #009fda;
  text-decoration: none;
}
</style>
