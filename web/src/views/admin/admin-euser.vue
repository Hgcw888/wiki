<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >


      <a-form layout="inline" :model="param">
        <a-form-item>
          <a-input v-model:value="param.loginName" placeholder="请输入名称">
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery({page:1,size:pagination.pageSize})">
            查询
          </a-button>
          <a-button type="primary" @click="handleQuery1({page:1,size:pagination.pageSize})" style="left: 20px">
            重置
          </a-button>
          <a-button type="primary" @click="add()" style="left: 40px">
            新增
          </a-button>
        </a-form-item>
      </a-form>


      <!--      pagination:分页-->
      <!--      loading:延迟加载-->
      <!--      row-key每一行的id-->
      <!--      row-key="record => record.id"指定键位，拿到数据与id-->
      <a-table :columns="columns"
               :row-key="record => record.id"
               :data-source="eusers"
               :pagination="pagination"
               :loading="loading"
               @change="handleTableChange"
      >
        <!--  将路径渲染成图片封面图片cover-->
        <template #cover="{ text: cover}">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <!--        讲两个分类作为一个组件-->
        <template v-slot:category="{text,record}">
          <span>{{ getCategoryName(record.category1Id) }}/{{ getCategoryName(record.category2Id) }}</span>
        </template>

        <!--删除，编辑按钮-->
        <template v-slot:action="{text,record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <!--            @confirm执行方法相当于 @click-->
            <!--            @cancel逻辑 没有的话就不写-->
            <a-popconfirm
                title="删除后不可以恢复"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="denger">
                删除
              </a-button>
            </a-popconfirm>

          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      title="编辑表单"
      v-model:visible="modelVisible"
      :confirm-loading="modelLoading"
      @ok="handleModelOk"
  >
    <a-form :model="euser" :label-col="{span:6}">
      <a-form-item label="登陆名">
<!--        disabled="euser.id"如果有值就不能修改，没有就可以-->
        <a-input v-model:value="euser.loginName" :disabled="!!euser.id"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="euser.name"/>
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="euser.password"/>
      </a-form-item>

    </a-form>
  </a-modal>
</template>
<script lang="ts">
import {defineComponent, onMounted, ref,} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
declare let hexMd5:any;
declare let KEY:any;


export default defineComponent({
  // 路由名称
  name: 'AdminEuser',
  // setup初始化方法

  setup() {

    //定于响应式变量接收查询输入的参数
    const param = ref();
    param.value = {};

    //定于响应式变量（ref()）不需要执行方法，改变的数据会随着你的改变而改变，接受数据
    const eusers = ref();

//定于分页大小和页数
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
//定义m默认延迟加载
    const loading = ref(false);

    const columns = [
      {
        title: '登陆名',
        dataIndex: 'loginName',
      },
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '密码',
        dataIndex: 'password',
      },
      {
        title: 'Action',
        key: 'action',
        slots: {
          customRender: 'action'
        }
      }
    ];




    /**
     * 获取euser列表，p为传回对象,传入axios的data请求中
     * @param params
     */
    const handleQuery = (params: any) => {
      loading.value = true;
      //情况数据，如果不情况现有的数据，则编辑报错从新加载后，在点击编辑，则列表显示的还是编辑签的数据
      eusers.value = [];
      axios.get("/euser/dimSelect", {
        params: {
          page: params.page,
          size: params.size,
          //传入接收到的查询参数
          loginName: param.value.loginName
        }
      }).then((response) => {

        loading.value = false;
        const date = response.data;
        //判断符合实体类中的参数校验
        if (date.success) {
          eusers.value = date.content.list;
          //重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = date.content.total
        } else {
          //如果没有的话返回实体类错误提示
          message.error(date.message)
        }
      });
    };

    /**
     * 重置
     * @param params
     */
    const handleQuery1 = (params: any) => {
      loading.value = true;

      axios.get("/euser/dimSelect", {

        params: {
          page: params.page,
          size: params.size,
        }
      }).then((response) => {

        loading.value = false;
        const date = response.data;
        //判断符合实体类中的参数校验
        if (date.success) {
          eusers.value = date.content.list;
          //重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = date.content.total
        } else {
          //如果没有的话返回实体类错误提示
          message.error(date.message)
        }
      });
    };


    /**
     * pagination传入对象
     * 点击页面码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("自带分页:" + pagination);
      //点击其它页是需要重新调用方法从新查询
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };
    /**
     * 表单弹框
     * euser接受表单回传数据
     * categoryIds数组【100,101】对应的就是：前端开发/vue
     */
    const euser = ref();
    const modelVisible = ref(false)//框
    const modelLoading = ref(false)
    const handleModelOk = () => {
      modelLoading.value = true;
      axios.post("/euser/updateEuser", euser.value).then((response) => {
        //密码加密传输应用js文件的md5.js中的方法
        euser.value.password=hexMd5(euser.value.password + KEY);

        //有返回数据的话就关闭modelLoading
        modelLoading.value = false;
        const data = response.data//commonResp数据
        //判断符合实体类中的参数校验
        if (data.success) {
          modelVisible.value = false;//框
          //添加成功后从新调用获取列表方法
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          //如果没有的话返回实体类错误提示
          message.error(data.message)
        }
      });
    };
    /**
     * 编辑
     * 将表单接收的到的参数record赋值到euser中
     */
    const edit = (record: any) => {
      modelVisible.value = true
      euser.value = Tool.copy(record);
    };
    /**
     * 删除电子书
     * 将表单接收的到的参数record赋值到euser中
     * any支持所有类型
     * Long类型使用number
     */
    const handleDelete = (id: number) => {
      axios.delete("/euser/delectEuser/" + id).then((response) => {
        const data = response.data//commonResp数据返回参数头
        if (data.success) {
          //添加成功后从新调用获取列表方法
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      });
    };

    /**
     * 新增
     * 表单为空数组，来接收eusers填入的参数
     */
    const add = () => {
      modelVisible.value = true
      euser.value = {};
    };


    //初始化去查询
    onMounted(() => {
      //初始化先查出所有分类
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });
    return {
      param,
      eusers,
      pagination,
      columns,
      loading,
      handleTableChange,
      edit,
      modelVisible,
      modelLoading,
      handleModelOk,
      euser,

      add,
      handleDelete,
      handleQuery,
      handleQuery1,
    }
  }
});
</script>
<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>

