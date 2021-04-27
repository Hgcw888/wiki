<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >


      <a-form layout="inline" :model="param">
        <a-form-item>
          <a-input v-model:value="param.name" placeholder="请输入名称">
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
               :data-source="ebooks"
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
            <router-link :to="'/admin/doc?ebookId='+ record.id">
              <a-button type="primary">
                文档管理
              </a-button>
            </router-link>


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
    <a-form :model="ebook" :label-col="{span:6}">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover"/>
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name"/>
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader v-model:value="categoryIds"
                    :field-names="{ label:'name',value:'id',children:'children'}"
                    :options="level1"
        />

      </a-form-item>

      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" style="height: 100px;width: 450px"/>
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
  name: 'AdminEbook',
  // setup初始化方法

  setup() {

    //定于响应式变量接收查询输入的参数
    const param = ref();
    param.value = {};

    //定于响应式变量（ref()）不需要执行方法，改变的数据会随着你的改变而改变，接受数据
    const ebooks = ref();

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
        title: '封面',
        dataIndex: 'cover',
        slots: {customRender: 'cover'}
      },
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '分类',
        slots: {customRender: 'category'}
      },
      {
        title: '文档数',
        dataIndex: 'docCount',
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount',
      },
      {
        title: '点赞数',
        dataIndex: 'docCount',
      },
      {
        title: 'Action',
        key: 'action',
        slots: {
          customRender: 'action'
        }
      }
    ];

    const level1 = ref();//一级分类树，children属性就是二级分类
    let categorys: any;//分类结合成一个显示使用的对象
    /**
     * 用来查询所有的分类，分类框的分类特用
     */
    const handleQueryCatergory = () => {
      loading.value = true;
      axios.get("/categoty/selectList").then((response) => {
        loading.value = false;
        const date = response.data;
        //判断符合实体类中的参数校验
        if (date.success) {
          categorys = date.content;
          //进行树形结构
          console.log("原始数组:", categorys);
          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构:", level1.value);
          //菜单列表再查询电子书列表
          handleQuery({
            page: 1,
            size: pagination.value.pageSize
          });

        } else {
          //如果没有的话返回实体类错误提示
          message.error(date.message)
        }
      });
    };

    /**
     * 讲分类合并成一个组显示。通过他的分类id相应分类级别查出名字
     *
     */
    const getCategoryName = (cid: number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          result = item.name;
        }
      });
      return result;
    };


    /**
     * 获取ebook列表，p为传回对象,传入axios的data请求中
     * @param params
     */
    const handleQuery = (params: any) => {
      loading.value = true;
      //情况数据，如果不情况现有的数据，则编辑报错从新加载后，在点击编辑，则列表显示的还是编辑签的数据
      ebooks.value = [];
      axios.get("/api/dimSelect", {

        params: {
          page: params.page,
          size: params.size,
          //传入接收到的查询参数
          name: param.value.name
        }
      }).then((response) => {

        loading.value = false;
        const date = response.data;
        //判断符合实体类中的参数校验
        if (date.success) {
          ebooks.value = date.content.list;
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
      param.value.name=null;
      axios.get("/api/dimSelect", {

        params: {
          page: params.page,
          size: params.size,
        }
      }).then((response) => {

        loading.value = false;
        const date = response.data;
        //判断符合实体类中的参数校验
        if (date.success) {
          ebooks.value = date.content.list;
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
     * ebook接受表单回传数据
     * categoryIds数组【100,101】对应的就是：前端开发/vue
     */
    const categoryIds = ref();
    const ebook = ref();
    const modelVisible = ref(false)//框
    const modelLoading = ref(false)
    const handleModelOk = () => {
      modelLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/api/updateEbook", ebook.value).then((response) => {
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
     * 将表单接收的到的参数record赋值到ebook中
     */
    const edit = (record: any) => {
      modelVisible.value = true
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
    };
    /**
     * 删除电子书
     * 将表单接收的到的参数record赋值到ebook中
     * any支持所有类型
     * Long类型使用number
     */
    const handleDelete = (id: number) => {
      axios.delete("/api/delectEbook/" + id).then((response) => {
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
     * 表单为空数组，来接收ebooks填入的参数
     */
    const add = () => {
      modelVisible.value = true
      ebook.value = {};
    };


    //初始化去查询
    onMounted(() => {
      //初始化先查出所有分类
      handleQueryCatergory();

    });
    return {
      categoryIds,
      level1,
      param,
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      edit,
      modelVisible,
      modelLoading,
      handleModelOk,
      ebook,

      add,
      handleDelete,
      handleQuery,
      handleQuery1,
      handleQueryCatergory,
      getCategoryName
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

