<template>
  <div id="questionsView" class="container">
    <a-card class="search-card">
      <a-form :model="searchParams" layout="inline">
        <a-form-item field="title" label="题目名称">
          <a-input v-model="searchParams.title" placeholder="请输入题目名称" allow-clear style="width: 240px" />
        </a-form-item>
        <a-form-item field="tags" label="标签">
          <a-input-tag v-model="searchParams.tags" placeholder="输入标签后回车" allow-clear style="width: 240px" />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" @click="doSubmit" :loading="loading">
              <template #icon><icon-search /></template>
              搜索
            </a-button>
            <a-button @click="resetSearch">
              <template #icon><icon-refresh /></template>
              重置
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>

    <a-divider margin="0" />

    <a-table :columns="columns" :data="dataList" :pagination="{
      showTotal: true,
      showPageSize: true,
      pageSize: searchParams.pageSize,
      current: searchParams.current,
      total,
    }" :loading="loading" :bordered="true" :stripe="true" @page-change="onPageChange"
      @page-size-change="onPageSizeChange">
      <template #empty>
        <a-empty description="暂无题目数据" />
      </template>

      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="arcoblue" size="small">
            {{ tag }}
          </a-tag>
        </a-space>
      </template>

      <template #acceptedRate="{ record }">
        <a-tooltip :content="`通过数: ${record.acceptedNum} | 提交数: ${record.submitNum}`">
          <a-progress :percent="record.submitNum
            ? Math.round((record.acceptedNum / record.submitNum) * 100)
            : 0
            " :size="'small'" :stroke-width="20" status="normal" :show-text="true" />
        </a-tooltip>
      </template>

      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm") }}
      </template>

      <template #optional="{ record }">
        <a-button type="outline" size="small" @click="toQuestionPage(record)" :loading="record.loading">
          <template #icon><icon-arrow-right /></template>
          开始做题
        </a-button>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionQueryRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const router = useRouter();
const loading = ref(false);
const dataList = ref<Question[]>([]);
const total = ref(0);

const searchParams = ref<QuestionQueryRequest>({
  title: "",
  tags: [],
  pageSize: 10,
  current: 1,
});

// 加载数据
const loadData = async () => {
  loading.value = true;
  try {
    const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
      searchParams.value
    );
    if (res.code === 0) {
      dataList.value = res.data.records;
      total.value = res.data.total;
    } else {
      message.error(`加载失败: ${res.message}`);
    }
  } catch (e) {
    message.error("加载题目列表失败，请重试");
  } finally {
    loading.value = false;
  }
};

// 监听搜索参数变化
watchEffect(() => {
  loadData();
});

// 列定义
const columns = [
  {
    title: "题号",
    dataIndex: "id",
    width: 100,
    align: "center",
  },
  {
    title: "题目名称",
    dataIndex: "title",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "标签",
    slotName: "tags",
    width: 200,
  },
  {
    title: "通过率",
    slotName: "acceptedRate",
    width: 200,
  },
  {
    title: "创建时间",
    slotName: "createTime",
    width: 180,
  },
  {
    title: "操作",
    slotName: "optional",
    width: 120,
    align: "center",
  },
];

// 分页变化
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

// 分页大小变化
const onPageSizeChange = (size: number) => {
  searchParams.value = {
    ...searchParams.value,
    pageSize: size,
    current: 1,
  };
};

// 跳转到做题页面
const toQuestionPage = (question: Question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};

// 执行搜索
const doSubmit = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};

// 重置搜索
const resetSearch = () => {
  searchParams.value = {
    title: "",
    tags: [],
    pageSize: 10,
    current: 1,
  };
};
</script>

<style scoped>
.container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px;
}

.search-card {
  margin-bottom: 16px;
  border-radius: 4px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
}

.search-card :deep(.arco-card-body) {
  padding: 16px 20px;
}

.arco-table {
  border-radius: 4px;
  overflow: hidden;
}

.arco-table :deep(.arco-table-th) {
  background-color: var(--color-fill-2);
  font-weight: 500;
}

.arco-table :deep(.arco-table-tr):hover {
  background-color: var(--color-fill-1);
}
</style>