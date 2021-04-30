<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <a-row :gutter="24">
        <a-col :span="8">
          <p>
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
          </p>


          <!--      pagination:分页-->
          <!--      loading:延迟加载-->
          <!--      row-key每一行的id-->
          <!--      row-key="record => record.id"指定键位，拿到数据与id-->
          <!--          :defaultExpandAllRows="true"  :key={tableKey}进入页面时和刷新时节点类展开，需要两个一起加-->
          <a-table
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :key={tableKey}
              :defaultExpandAllRows="true"
          >
            <!--  将路径渲染成图片封面图片cover-->
            <template #name="{ text,record}">
              {{ record.sort }}{{ text }}
            </template>

            <!--删除，编辑按钮-->
            <template v-slot:action="{text,record}">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
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
                  <a-button type="denger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>

              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>

          <a-form :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父类文档"
                  tree-default-expand-all
                  :replaceFields="{title:'name' ,key:'id',value:'id'}"
              >
              </a-tree-select>
            </a-form-item>

            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="排序"/>
            </a-form-item>

            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeQutlined/>内容预览
              </a-button>
            </a-form-item>

            <a-form-item>
              <div id="content"/>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
  <!--  <a-modal-->
  <!--      title="文档表单"-->
  <!--      v-model:visible="modelVisible"-->
  <!--      :confirm-loading="modelLoading"-->
  <!--      @ok="handleModelOk"-->
  <!--  >-->
  <!--  </a-modal>-->
</template>
<script lang="ts">
import {defineComponent, onMounted, ref,} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import {Modal} from 'ant-design-vue';
import E from "wangeditor";


export default defineComponent({
  // 路由名称
  name: 'AdminDoc',
  // setup初始化方法

  setup() {
    //点击按钮页面跳转接受路由的传递的信息
    const route = useRoute();
    // console.log("rout.query", route.query);//路由传递的参数
    //定于响应式变量接收查询输入的参数
    const param = ref();
    param.value = {};

    //定于响应式变量（ref()）不需要执行方法，改变的数据会随着你的改变而改变，接受数据
    const docs = ref();

//定义m默认延迟加载
    const loading = ref(false);

    //因为树选择主键的树形状态，会随着当前编辑的节点而变化，所有单独
    const treeSelectData = ref();
    treeSelectData.value = [];

    const columns = [

      {
        title: '名称',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      // {
      //   title: '父文档',
      //   key: 'parent',
      //   dataIndex: 'parent',
      // },

      // {
      //   title: '顺序',
      //   dataIndex: 'sort',
      // },
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

      editor.txt.html("");
      loading.value = true;
      //情况数据，如果不情况现有的数据，则编辑报错从新加载后，在点击编辑，则列表显示的还是编辑签的数据
      level1.value = [];
      axios.get("/doc/selectList/" + route.query.ebookId).then((response) => {
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
          //父类文档下拉初始化，相当于点击新增
          treeSelectData.value = Tool.copy(level1.value);
          //选择树添加一个‘无’
          treeSelectData.value.unshift({id: 0, name: '无'})
        } else {
          //如果没有的话返回实体类错误提示
          message.error(date.message)
        }
      });
    };

    const setDisable = (treeSelectData: any, id: any) => {
      //遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id == id) {
          //如果点前节点就是目标节点
          console.log("disabled", node);
          //将目标节点设置威disabled
          node.disabled = true;

          //遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          //如果当前节点不是目标节点，则到其他节点再找
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    }


    /**
     * 表单弹框
     * doc接受表单回传数据
     */

    const doc = ref();
    doc.value = {
      //初始化就获取ID，避免需要点击新增
      ebookId: route.query.ebookId
    };
    const modelVisible = ref(false)//框
    const modelLoading = ref(false)
    const editor = new E("#content");//富文本容器
    editor.config.zIndex = 0;

    const handleSave = () => {
      //获取超链接中的html就是文档内容
      doc.value.content = editor.txt.html();
      modelLoading.value = true;
      axios.post("/doc/updateDoc", doc.value).then((response) => {
        //有返回数据的话就关闭modelLoading
        modelLoading.value = false;
        const data = response.data//commonResp数据
        //判断符合实体类中的参数校验
        if (data.success) {


          message.success("编辑成功")
          //添加成功后从新调用获取列表方法
          handleQuery();
        } else {
          //如果没有的话返回实体类错误提示
          message.error(data.message)
        }
      });
    };


    /**
     * 文档内容查询
     * */
    const handleQueryContent = () => {

      axios.get("/doc/find-content/" + doc.value.id).then((response) => {
        const date = response.data;
        //判断符合实体类中的参数校验
        if (date.success) {
          // 赋值给富文本
          editor.txt.html(date.content)
        } else {
          //如果没有的话返回实体类错误提示
          message.error(date.message)
        }
      });
    };


    /**
     * 编辑
     * 将表单接收的到的参数record赋值到doc中
     */
    const edit = (record: any) => {
      //先清空富文本框数据
      editor.txt.html("");
      modelVisible.value = true
      doc.value = Tool.copy(record);
      handleQueryContent();
      //不能选择当前节点所有子节点作为父节点，不然会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id)
      //作为选择树添加一个“无”
      treeSelectData.value.unshift({id: 0, name: '无'});
      // setTimeout(function () {
      //   editor.create();//创建文本
      // }, 100);
    };


    /**
     * 删除父节点的时候将其子类全部删除掉,所以要查找整个节点的id
     */
    const ids: Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
      //遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id == id) {
          //如果点前节点就是目标节点
          console.log("disabled", node);
          //将目标节点设置威disabled
          node.disabled = true;
          ids.push(id);

          //遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          //如果当前节点不是目标节点，则到其他节点再找
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    }


    /**
     * 删除文档
     * 将表单接收的到的参数record赋值到doc中
     * any支持所有类型
     * Long类型使用number
     */
    const handleDelete = (id: number) => {
      //
      // Modal.confirm({
      //   title: 'Do you want to delete these items?',
      //   content: 'When clicked the OK button, this dialog will be closed after 1 second',
      //   onOk() {
      //     return new Promise((resolve, reject) => {
      //       setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
      //     }).catch(() => console.log('Oops errors!'));
      //   },
      //   // eslint-disable-next-line @typescript-eslint/no-empty-function
      //   onCancel() {},
      // });


      getDeleteIds(level1.value, id);//调用方法获取整个节点的所有节点的id进行查询
      axios.delete("/doc/delectDoc/" + ids.join(",")).then((response) => {
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
      //先清空富文本框数据
      editor.txt.html("");
      modelVisible.value = true
      doc.value = {
        //获取路由传第的信息获取电子书的id
        ebookId: route.query.ebookId
      };
      treeSelectData.value = Tool.copy(level1.value);

      //作为选择树添加一个“无”
      treeSelectData.value.unshift({id: 0, name: '无'});
      // setTimeout(function () {
      //   editor.create();//创建文本
      // }, 100);

    };

    // ----------------富文本预览--------------
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };
    //初始化去查询
    onMounted(() => {
      handleQuery();
      editor.create();//创建文本
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
      doc,
      treeSelectData,

      add,
      handleDelete,
      handleQuery,
      handleSave,
      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
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

