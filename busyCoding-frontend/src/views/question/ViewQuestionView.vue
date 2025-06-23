<template>
  <div id="viewQuestionView">
    <!-- 顶部标题区域 -->
    <div class="header">
      <a-typography-title :level="2" class="title">
        {{ question?.title || '题目详情' }}
      </a-typography-title>
      <a-space wrap>
        <a-tag v-for="(tag, index) of question?.tags" :key="index" color="arcoblue" bordered>
          {{ tag }}
        </a-tag>
      </a-space>
    </div>

    <a-row :gutter="[24, 24]" class="content-row">
      <!-- 左侧题目详情 -->
      <a-col :md="12" :xs="24">
        <a-card class="info-card">
          <a-tabs default-active-key="question" type="rounded">
            <a-tab-pane key="question" title="题目描述">
              <div class="problem-content">
                <MdViewer :value="question?.content || '题目加载中...'" />
              </div>

              <a-divider margin="20px 0" />

              <a-descriptions title="判题条件" :column="{ xs: 1, md: 3 }" layout="inline-horizontal" bordered>
                <a-descriptions-item label="时间限制">
                  <a-tag color="orangered">{{ question?.judgeConfig?.timeLimit || 0 }} ms</a-tag>
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  <a-tag color="purple">{{ question?.judgeConfig?.memoryLimit || 0 }} MB</a-tag>
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  <a-tag color="green">{{ question?.judgeConfig?.stackLimit || 0 }} MB</a-tag>
                </a-descriptions-item>
              </a-descriptions>
            </a-tab-pane>

            <a-tab-pane key="comment" title="评论区">
              <div class="comment-placeholder">
                <a-empty description="评论区即将开放">
                  <template #image>
                    <icon-message />
                  </template>
                </a-empty>
              </div>
            </a-tab-pane>

            <a-tab-pane key="answer" title="参考答案">
              <div class="answer-placeholder">
                <a-result status="403" subtitle="参考答案暂不开放，请先尝试独立解题">
                  <template #icon>
                    <icon-lock />
                  </template>
                </a-result>
              </div>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>

      <!-- 右侧代码编辑区域 -->
      <a-col :md="12" :xs="24">
        <a-card class="code-card">
          <a-form :model="form" layout="vertical">
            <a-form-item label="选择编程语言" field="language">
              <a-select v-model="form.language" placeholder="请选择编程语言" allow-clear>
                <a-option value="java">Java</a-option>
                <a-option value="cpp">C++</a-option>
                <a-option value="python">Python</a-option>
                <a-option value="javascript">JavaScript</a-option>
                <a-option value="go">Go</a-option>
                <a-option value="html">HTML/CSS</a-option>
              </a-select>
            </a-form-item>

            <a-form-item label="编写代码">
              <div class="code-editor-container">
                <CodeEditor :value="form.code as string" :language="form.language" :handle-change="changeCode"
                  :height="400" />
              </div>
            </a-form-item>

            <a-form-item>
              <a-button type="primary" long size="large" @click="doSubmit" :loading="submitting"
                :disabled="!form.code || !form.language">
                <template #icon>
                  <icon-send />
                </template>
                提交代码
              </a-button>
            </a-form-item>
          </a-form>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { defineProps, withDefaults } from 'vue';
import message from "@arco-design/web-vue/es/message";
import CodeEditor from "@/components/CodeEditor.vue";
import MdViewer from "@/components/MdViewer.vue";
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionVO,
} from "../../../generated";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

const question = ref<QuestionVO>();
const submitting = ref(false);

const loadData = async () => {
  try {
    const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
      props.id as any
    );
    if (res.code === 0) {
      question.value = res.data;
    } else {
      message.error("加载失败，" + res.message);
    }
  } catch (e) {
    message.error("加载题目详情失败，请重试");
  }
};

const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: "// 请在此处编写你的代码\npublic class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}",
});

/**
 * 提交代码
 */
const doSubmit = async () => {
  if (!question.value?.id || !form.value.code || !form.value.language) {
    return;
  }

  submitting.value = true;

  try {
    const res = await QuestionControllerService.doQuestionSubmitUsingPost({
      ...form.value,
      questionId: question.value.id,
    });
    if (res.code === 0) {
      message.success("提交成功，正在判题中...");
    } else {
      message.error("提交失败: " + res.message);
    }
  } catch (e) {
    message.error("提交失败，请重试");
  } finally {
    submitting.value = false;
  }
};

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const changeCode = (value: string) => {
  form.value.code = value;
};
</script>

<style scoped>
#viewQuestionView {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  margin-bottom: 24px;
  padding: 16px 20px;
  background-color: var(--color-bg-2);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.title {
  margin: 0;
  color: var(--color-text-1);
}

.content-row {
  margin-top: 12px;
}

.info-card,
.code-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  height: 100%;
}

.info-card :deep(.arco-card-body) {
  padding: 20px;
}

.code-card :deep(.arco-card-body) {
  padding: 20px;
}

.problem-content {
  padding: 20px;
  max-height: 500px;
  overflow-y: auto;
}

.code-editor-container {
  width: 100%;
  border: 1px solid var(--color-border-2);
  border-radius: 4px;
  overflow: hidden;
}

.comment-placeholder,
.answer-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

:deep(.arco-tabs-content) {
  padding: 0 20px 20px;
}

:deep(.arco-descriptions) {
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .header {
    padding: 12px;
  }

  .content-row {
    margin-top: 0;
  }

  .problem-content {
    padding: 12px;
    max-height: none;
  }

  .code-card :deep(.arco-card-body) {
    padding: 15px;
  }
}
</style>