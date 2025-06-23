<template>
  <div id="userLoginView">
    <el-card class="box-card w-margin">
      <img class="logo" src="../../assets/logo_nav.png" alt="">
      <el-form :model="form">
        <el-form-item>
          <el-input v-model="form.userAccount" placeholder="账号" />
        </el-form-item>
        <el-form-item>
          <el-input type="password" v-model="form.userPassword" placeholder="请输入8位及以上密码" />
        </el-form-item>
        <el-form-item>
          <el-button class="login_btn" type="primary" round @click="handleSubmit()">登录/注册</el-button>
        </el-form-item>
      </el-form>
      <el-text class="protocol">注册或登录即代表您同意《用户协议》和《隐私协议》</el-text>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import { ElCard, ElForm, ElButton, ElFormItem, ElText, ElInput } from 'element-plus'
import { UserControllerService, UserLoginRequest } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

/**
 * 表单信息
 */
const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);

const router = useRouter();
const store = useStore();

/**
 * 提交表单
 * @param data
 */
const handleSubmit = async () => {
  const res = await UserControllerService.userLoginUsingPost(form);
  // 登录成功，跳转到主页
  if (res.code === 0) {
    if (res.data.jwt)
      localStorage.setItem("token", res.data.jwt);
    await store.dispatch("user/getLoginUser");
    router.push({
      path: "/",
      replace: true,
    });
  } else {
    message.error("登录失败，" + res.message);
  }
};
</script>

<style lang="scss" scoped>
#userLoginView {
  display: flex;
  justify-content: center;
  margin-top: 60px;
}

:deep(.el-dialog) {
  border-radius: 15px;
}

:deep(.el-form-item__label) {
  font-size: 18px;
}

:deep(.el-card__body) {
  padding: 50px 40px;
}

:deep(.el-input__inner) {
  height: 42px;
}

.box-card {
  width: 26%;
  height: 500px;

  .logo {
    height: 42px;
    margin-bottom: 30px;
  }

  .wxLogin {
    display: flex;

    span {
      font-size: 16px;
    }

    .wx_img {
      cursor: pointer;
      margin-left: 30px;
    }
  }

  .login_btn {
    width: 100%;
    height: 45px;
    margin-top: 40px;
  }

  .protocol {
    margin-top: 40px;
    font-size: 14px;
  }
}

:deep(.el-dialog--center .el-dialog__body) {
  text-align: center;
}
</style>