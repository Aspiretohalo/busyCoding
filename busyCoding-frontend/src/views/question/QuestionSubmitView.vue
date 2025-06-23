<template>
  <div id="questionSubmitView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="questionId" label="题号" style="min-width: 240px">
        <a-input v-model="searchParams.questionId" placeholder="请输入题目ID" allow-clear />
      </a-form-item>
      <a-form-item field="language" label="编程语言" style="min-width: 240px">
        <a-select v-model="searchParams.language" :style="{ width: '200px' }" placeholder="选择编程语言" allow-clear>
          <a-option value="java">Java</a-option>
          <a-option value="cpp">C++</a-option>
          <a-option value="go">Go</a-option>
          <a-option value="html">HTML</a-option>
          <a-option value="python">Python</a-option>
          <a-option value="javascript">JavaScript</a-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit" :loading="loading">搜索</a-button>
        <a-button style="margin-left: 8px" @click="resetSearch">重置</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table :columns="columns" :data="dataList" :pagination="{
      showTotal: true,
      showPageSize: true,
      pageSize: searchParams.pageSize,
      current: searchParams.current,
      total,
    }" :loading="loading" :bordered="true" @page-change="onPageChange" @page-size-change="onPageSizeChange">
      <template #empty>
        <a-empty description="暂无提交记录" />
      </template>

      <template #judgeInfo="{ record }">
        <div v-if="record.judgeInfo">
          <a-tag v-if="record.status === 2" color="green">通过</a-tag>
          <a-tag v-else-if="record.status === 4" color="red">编译错误</a-tag>
          <a-tag v-else-if="record.status === 5" color="orange">超时</a-tag>
          <a-tag v-else-if="record.status === 6" color="purple">内存超限</a-tag>
          <a-tag v-else color="gray">{{ getStatusText(record.status) }}</a-tag>

          <div v-if="record.status === 2" class="info-detail">
            <span>时间: {{ record.judgeInfo.time || 0 }}ms</span>
            <span style="margin-left: 8px">内存: {{ record.judgeInfo.memory || 0 }}KB</span>
          </div>
          <div v-else-if="record.judgeInfo.message" class="error-message">
            {{ record.judgeInfo.message }}
          </div>
        </div>
        <span v-else>{{ getStatusText(record.status) }}</span>
      </template>

      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitQueryRequest
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import moment from "moment";

const loading = ref(false);
const dataList = ref([]);
const total = ref(0);
const searchParams = ref<QuestionSubmitQueryRequest>({
  questionId: undefined,
  language: undefined,
  pageSize: 10,
  current: 1,
});

// 状态码映射
const statusMap = {
  0: "等待中",
  1: "判题中",
  2: "成功",
  3: "系统错误",
  4: "编译错误",
  5: "运行超时",
  6: "内存超限",
  7: "部分通过"
};

// 获取状态文本
const getStatusText = (status: number) => {
  return statusMap[status] || "未知状态";
};

// 加载数据
const loadData = async () => {
  loading.value = true;
  try {
    const res = await QuestionControllerService.listQuestionSubmitByPageUsingPost({
      ...searchParams.value,
      sortField: "createTime",
      sortOrder: "descend",
    });

    if (res.code === 0) {
      dataList.value = res.data.records;
      total.value = res.data.total;
    } else {
      message.error(`加载失败: ${res.message}`);
    }
  } catch (e) {
    message.error("请求失败，请重试");
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
    title: "提交ID",
    dataIndex: "id",
    width: 100,
    ellipsis: true,
    tooltip: true
  },
  {
    title: "题目ID",
    dataIndex: "questionId",
    width: 100
  },
  {
    title: "编程语言",
    dataIndex: "language",
    width: 120,
    render: ({ record }) => {
      const langMap = {
        java: "Java",
        cpp: "C++",
        go: "Go",
        html: "HTML",
        python: "Python",
        javascript: "JavaScript"
      };
      return langMap[record.language] || record.language;
    }
  },
  {
    title: "判题结果",
    slotName: "judgeInfo",
    width: 240
  },
  {
    title: "提交者",
    dataIndex: "userId",
    width: 100,
    ellipsis: true,
    tooltip: true
  },
  {
    title: "提交时间",
    slotName: "createTime",
    width: 180
  }
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
    questionId: undefined,
    language: undefined,
    pageSize: 10,
    current: 1,
  };
};
</script>

<style scoped>
#questionSubmitView {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px;
}

.info-detail {
  margin-top: 6px;
  font-size: 12px;
  color: #666;
}

.error-message {
  margin-top: 6px;
  font-size: 12px;
  color: #f53f3f;
  white-space: pre-wrap;
}
</style>