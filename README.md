## 项目介绍
基于 SpringBoot 3 + LangChain4j 的 AI 零代码应用生成平台。用户输入自然语言描述，由 AI Agent 自动执行并发素材收集、代码生成、质量检查、项目构建的完整工作流，最终一键部署为可访问的 Web 应用。
## 目录结构
前端项目：ai-code-frontend，微服务重构：ai-code-microservice。 

后端项目在src下。

CURL是接口测试脚本，用于在本地测试 SSE 流式输出。

grafana是 可视化面板配置的 json 文件，可一键导入生成 grafana 可视化面板。

sql文件存放在 sql 目录下。注意 MySQL 版本选择 8.0+。

如果需要配置 promethues，可以选择导入 promethues.yml 文件。
```
ai-code
├── ai                --  Ai service模块，提供 Ai 服务
├── annotation        --  自定义权限注解
├── aop               --  权限注解切面类
├── captcha           --  图形验证码
├── common            --  通用模块
├── config            --  全局配置模块
├── constant          --  常量
├── controller        --  controller 模块
├── core              --  核心模块，用于 Ai 生成代码的解析保存和部署
├── easyexcel         --  excel 导入导出
├── exception         --  异常模块，存放自定义异常，全局异常处理器
├── generator         --  代码生成器
├── langgraph4j       --  Ai 工作流模块
├── manager           --  通用服务模块
├── mapper            --  mapper 模块
├── model             --  POJO 模块
├── monitor           --  可视化监控模块
├── rateLimiter       --  Redisson 限流
├── service           --  service 模块
├── utils             --  工具类
├── AiCodeApplication --  应用启动类
```
## 项目特点
1）代码生成：用户通过输入自然语言描述，AI 自动分析并选择合适的生成策略，通过工具调用生成代码文件，采用流式输出让用户实时看到 AI 的执行过程。

2）可视化编辑：生成的应用将实时展示，可以进入编辑模式，自由选择网页元素和 AI 进行对话来快速修改页面，知道满意为止。

3）一键部署：用户可以一键部署应用，生成可访问的 Web 应用，并支持一键导出部署包。

4）企业级管理：提供用户管理、应用管理、系统监控、业务指标监控等后台功能，管理员可以设置精选应用、监控 AI 调用情况和系统性能。
## 项目截图
首页，包括自然语言输入、我的作品、精选作品、（用户管理、应用管理等管理页面需要管理员权限）

![image](/static/img1.png)

![image](/static/img3.png)

![image](/static/img1.png)

登录页，可以点击“去注册”，注册完成后回到登录页面进行登录

![image](/static/img2.png)

Ai 应用对话页，在主页输入框输入你想要生成的应用描述（越详细越好），将进入对话页面，在这里实时输出 Ai 的结果，并在生成完成后展示网站。

![image](/static/img5.png)

点击输入框的编辑按钮，进入编辑模式。

![image](/static/img6.png)

此外还可以选择下载代码文件、部署等等。

用户管理

![image](/static/img7.png)

应用管理

![image](/static/img8.png)
## 技术介绍

**前端：** vue3.0 +  Pina + vue-router + axios + ant-design-vue

**后端：** SpringBoot3 + LangChain4j + nginx + Swagger3 + MyBatisFlex + Mysql8.0 + Redis

**其他：** 接入 COS 对象存储、deepseek 大模型、通义千问大模型、阿里云百炼文生图模型