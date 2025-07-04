import { createApp } from "vue";
import App from "./App.vue";
import ArcoVue from "@arco-design/web-vue";
import "@arco-design/web-vue/dist/arco.css";
import router from "./router";
import store from "./store";
import "@/plugins/axios";
import "@/access";
import "bytemd/dist/index.css";
import 'element-plus/dist/index.css'

const app = createApp(App)

app.use(ArcoVue)
    .use(store)
    .use(router)
    .mount("#app");
