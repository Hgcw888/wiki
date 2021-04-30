<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a-menu
        theme="dark"
        mode="horizontal"
        v-model:selectedKeys="selectedKeys1"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/euser" :style="euser.id ? {}:{ display: 'none'}">
        <router-link to="/admin/euser">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook" :style="euser.id ? {} :{ display:'none'}">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category" :style="euser.id ? {} :{display:'none'}">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>


      <a-popconfirm
          title="确认退出登录"
          ok-text="是"
          cancel-text="否"
          @confirm="logout()"
      >
        <a class="login-menu" v-show="euser.id">
          <span>退出登录</span>
        </a>
      </a-popconfirm>
      <a class="login-menu" v-show="euser.id">
        <span>您好：{{ euser.name }}</span>
      </a>

      <a class="login-menu" v-show="!euser.id" @click="showLoginModal">
        <span>登录</span>
      </a>

    </a-menu>
    <!--    弹框模式:loginModalVisible为弹框是否要弹出在方法中设置true为弹出，loginModalLoading点击确定后是否加载-->
    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login">
      <a-form :model="loginEuser" :label-col="{span: 6}" :wrapper-col="{span: 8}">
        <a-form-item label="登录名">
          <a-input v-model:value="loginEuser.loginName"/>
        </a-form-item>
        <a-form-item label="密码">
          <!--          //type="password"隐蔽密码-->
          <a-input v-model:value="loginEuser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>

  </a-layout-header>
</template>
<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import store from '@/store';

// 密码传输加密
declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup() {


//登录成功后接受登录这的信息,全局变量监听
    const euser = computed(() => store.state.euser);

    //loginEuser登录输入的值
    const loginEuser = ref({
      loginName: "",
      password: ""
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    const login = () => {
      console.log("开始登录")
      loginModalLoading.value = true;
      //密码传输加密
      loginEuser.value.password = hexMd5(loginEuser.value.password + KEY);
      axios.post('/euser/login', loginEuser.value).then((response) => {

        //获取到数据或关闭加载
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          //有值代表成功了，关闭弹框
          loginModalVisible.value = false;
          message.success("登陆成功");
          //将登录这的信息赋值到全局变量,传入setEuser方法
          store.commit("setEuser", data.content);

        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 登出
     */
    const logout = () => {
      console.log("开始登出")
      axios.get('/euser/logout/' + euser.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("登出成功");
          //登出后传入空值到全局变量,传入setEuser方法
          store.commit("setEuser", {});
        } else {
          message.error(data.message);
        }
      });
    };


    return {
      loginEuser,
      loginModalVisible,
      loginModalLoading,
      euser,
      showLoginModal,
      login,
      logout,

    }
  }

});
</script>
<style>
.login-menu {
  float: right;
  color: white;
  padding-left: 20px;
}
</style>