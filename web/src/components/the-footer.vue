<template>
  <a-layout-footer style="text-align: center">
    ©2021 此系统归 Hgcw 所属,只进行练习演示不用作任何商业<span v-show="euser.id">:感谢用户 {{ euser.name }} 您的使用</span>
  </a-layout-footer>
</template>
<script lang="ts">
import {computed, defineComponent} from 'vue';
import store from '@/store';


export default defineComponent({
  name: 'the-footer',
  setup() {
    const euser = computed(() => store.state.euser);
    return{
      euser,
    }
  }

});
</script>