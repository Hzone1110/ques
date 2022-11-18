<template>
  <div class="login">
    <div
      class="login-wrap"
      style="background: #fff; padding: 20px; border-radius: 10px"
    >
      <h3 class="title">问卷系统-答者注册</h3>

      <el-form ref="form" :model="form" :rules="rules" class="login-form">
        <el-form-item prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入用户名">
            <svg-icon
              slot="prefix"
              icon-class="phone"
              class="el-input__icon input-icon"
            />
          </el-input>
        </el-form-item>
        <el-form-item prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入姓名">
            <svg-icon
              slot="prefix"
              icon-class="user"
              class="el-input__icon input-icon"
            />
          </el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="sex">
              <el-select
                v-model="form.sex"
                placeholder="请选择性别"
                style="width: 100%"
              >
                 <template #prefix>
                   <svg-icon
                  slot="prefix"
                  icon-class="tool"
                  class="el-input__icon input-icon"
                />
                </template>
                <el-option
                  v-for="item in sexOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="age">
              <el-input
                type="number"
                v-model="form.age"
                placeholder="请输入年龄"
              >
                <svg-icon
                  slot="prefix"
                  icon-class="radio"
                  class="el-input__icon input-icon"
                />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="job">
              <el-input v-model="form.job" placeholder="请输入职业" maxlength="15">
                <svg-icon
                  slot="prefix"
                  icon-class="post"
                  class="el-input__icon input-icon"
                />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="like">
              <el-input v-model="form.like"  maxlength="15" placeholder="请输入爱好">
                <svg-icon
                  slot="prefix"
                  icon-class="theme"
                  class="el-input__icon input-icon"
                />
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱">
            <svg-icon
              slot="prefix"
              icon-class="email"
              class="el-input__icon input-icon"
            />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
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

        <span class="regc" @click="toLogin">已有账号？去登录</span>
        <el-form-item style="width: 100%">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width: 100%"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">注 册</span>
            <span v-else>注 册 中...</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2022 Penint All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { registerAnswerer } from "@/api/login";
import RegionSelect from "./components/RegionSelect";

// 设置手机号的验证规则
const phone = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入手机号"));
  } else {
    const reg = /^1[3|4|5|6|7|8][0-9]\d{8}$/;
    // const phoneReg = /^1[34578]\d{9}$/
    if (reg.test(value)) {
      callback();
    } else {
      return callback(new Error("请输入正确的手机号"));
    }
  }
};
// 邮箱
var email = (rule, value, callback) => {
  const mal = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  // const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
  if (rule.required && !value) {
    return callback(new Error("不能为空"));
  }
  if (value) {
    if (!mal.test(value)) {
      callback(new Error("请输入正确邮箱"));
    } else {
      callback();
    }
  }
};

export default {
  name: "Login",
  components: { RegionSelect },

  data() {
    return {
      sexOptions: [
        {
          value: "0",
          label: "男",
        },
        {
          value: "1",
          label: "女",
        },
      ],
      codeUrl: "",
      cookiePassword: "",
      form: {
        password: "",
        phonenumber: "",
        email: "",
      },
      rules: {
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" },
        ],
        nickName: [
          { required: true, trigger: "blur", message: "姓名不能为空" },
        ],
        sex: [
          { required: true, trigger: "change", message: "性别不能为空" },
        ],
        age: [
          { required: true, trigger: "blur", message: "年龄不能为空" },
        ],
        job: [
          { required: true, trigger: "blur", message: "职业不能为空" },
        ],
        like: [
          { required: true, trigger: "blur", message: "爱好不能为空" },
        ],
        phonenumber: [{ required: true, trigger: "blur", validator: phone }],
        email: [{ required: true, trigger: "blur", validator: email }],
      },
      loading: false,
      redirect: undefined,
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
  created() {},
  methods: {
    toLogin() {
      this.$router.push("/login");
    },
    handleLogin() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          registerAnswerer(this.form).then((res) => {
            if (res.code == 200) {
              this.msgSuccess("注册答者成功，即将跳转登录页！");
              setTimeout(() => {
                this.toLogin();
              }, 2000);
            }
          });
        }
      });
    },
    isvalidatemobile(mobile) {
      var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (!myreg.test(mobile)) {
        return false;
      } else {
        return true;
      }
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
  width: 600px;
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
