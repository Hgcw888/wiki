<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >


      <a-form layout="inline" :model="param">

        <a-form-item>
          <a-button type="primary" @click="handleQuery()">
            刷新
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
               :data-source="level1"
               :loading="loading"
               :pagination="false"
      >
        <!--  将路径渲染成图片封面图片cover-->
        <template #cover="{ text: cover}">
          <img v-if="cover" :src="cover" alt="avatar"/>
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
      title="文档表单"
      v-model:visible="modelVisible"
      :confirm-loading="modelLoading"
      @ok="handleModelOk"
  >
    <a-form :model="doc" :label-col="{span:6}" :wrapper-col="{span: 18}">

      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>

      </a-form-item>
      <a-form-item label="父文档">
<!--        <a-input v-model:value="doc.parent"/>-->
        <a-select
            v-model:value="doc.parent"
            ref="select"
        >
          <a-select-option value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="doc.id === c.id">{{ c.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="排序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts">
import {defineComponent, onMounted, ref,} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";


export default defineComponent({
  // 路由名称
  name: 'AdminDoc',
  // setup初始化方法

  setup() {

    //定于响应式变量接收查询输入的参数
    const param = ref();
    param.value = {};

    //定于响应式变量（ref()）不需要执行方法，改变的数据会随着你的改变而改变，接受数据
    const docs = ref();

//定义m默认延迟加载
    const loading = ref(false);

    const columns = [

      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '父文档',
        key: 'parent',
        dataIndex: 'parent',
      },

      {
        title: '顺序',
        dataIndex: 'sort',
      },
      {
        title: 'Action',
        key: 'action',
        slots: {
          customRender: 'action'
        }
      }
    ];


    const level1 = ref();//一级文档树，children属性就是二级文档
    /**
     * 获取doc列表，p为传回对象,传入axios的data请求中
     * @param params
     */
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/selectList").then((response) => {
        loading.value = false;
        const date = response.data;
        //判断符合实体类中的参数校验
        if (date.success) {
          docs.value = date.content;
          //进行树形结构
          console.log("原始数组:", docs.value);
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("树形结构:", level1);
        } else {
          //如果没有的话返回实体类错误提示
          message.error(date.message)
        }
      });
    };

    /**
     * 表单弹框
     * doc接受表单回传数据
     */
    const doc = ref({});
    const modelVisible = ref(false)//框
    const modelLoading = ref(false)
    const handleModelOk = () => {
      modelLoading.value = true;
      axios.post("/doc/updateDoc", doc.value).then((response) => {
        //有返回数据的话就关闭modelLoading
        modelLoading.value = false;
        const data = response.data//commonResp数据
        //判断符合实体类中的参数校验
        if (data.success) {
          modelVisible.value = false;//框
          //添加成功后从新调用获取列表方法
          handleQuery();
        } else {
          //如果没有的话返回实体类错误提示
          message.error(data.message)
        }
      });
    };
    /**
     * 编辑
     * 将表单接收的到的参数record赋值到doc中
     */
    const edit = (record: any) => {
      modelVisible.value = true
      doc.value = Tool.copy(record);
    };
    /**
     * 删除文档
     * 将表单接收的到的参数record赋值到doc中
     * any支持所有类型
     * Long类型使用number
     */
    const handleDelete = (id: number) => {
      axios.delete("/doc/delectDoc/" + id).then((response) => {
        const data = response.data//commonResp数据返回参数头
        if (data.success) {
          //添加成功后从新调用获取列表方法
          handleQuery();
        }
      });
    };

    /**
     * 新增
     * 表单为空数组，来接收docs填入的参数
     */
    const add = () => {
      modelVisible.value = true
      doc.value = {};
    };


    //初始化去查询
    onMounted(() => {
      handleQuery();
    });
    return {
      level1,
      param,
      // docs,
      columns,
      loading,
      edit,
      modelVisible,
      modelLoading,
      handleModelOk,
      doc,

      add,
      handleDelete,
      handleQuery,
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

