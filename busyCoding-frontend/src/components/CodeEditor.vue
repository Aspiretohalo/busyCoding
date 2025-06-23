<template>
  <div id="code-editor" ref="codeEditorRef" style="min-height: 400px; height: 60vh" />
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import { onMounted, ref, toRaw, withDefaults, defineProps, watch } from "vue";

/**
 * 定义组件属性类型
 */
interface Props {
  value: string;
  language?: string;
  handleChange: (v: string) => void;
}

/**
 * 给组件指定初始值
 * 这里将默认值设置为 class Main() { }，并指定默认语言为 Java
 */
const props = withDefaults(defineProps<Props>(), {
  value: () => "class Main{\n\n\tpublic static void main(String[] args) {}\n}",
  language: () => "java",
  handleChange: (v: string) => {
    console.log(v);
  },
});

const codeEditorRef = ref();
const codeEditor = ref();

/**
 * 监听语言变化，当语言改变时，动态更新编辑器的语言
 */
watch(
  () => props.language,
  () => {
    if (codeEditor.value) {
      monaco.editor.setModelLanguage(
        toRaw(codeEditor.value).getModel(),
        props.language
      );
    }
  }
);

/**
 * 当组件挂载时，初始化 Monaco 编辑器，并使用初始值填充内容
 */
onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }

  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    // value: props.value, // 设置初始内容
    value: props.language == 'java' ? 'class Main{\n\n\tpublic static void main(String[] args) {}\n}' : props.value, // 设置初始内容
    language: props.language,
    automaticLayout: true,
    colorDecorators: true,
    minimap: {
      enabled: true,
    },
    readOnly: false,
    theme: "vs-dark",
  });

  // 监听编辑器内容的变化，并调用回调函数
  codeEditor.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditor.value).getValue());
  });
});
</script>

<style scoped></style>
