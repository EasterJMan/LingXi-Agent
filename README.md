# AI Agent

基于 **Spring Boot 3** 与 **Spring AI** 构建的 Java AI 智能体项目，集成阿里云通义千问（DashScope）、RAG 知识库增强、工具调用（Function Calling）以及 MCP（Model Context Protocol）客户端/服务端能力。项目包含一个恋爱咨询示例应用（LoveApp）和通用自主 Agent（YuManus）。

## 功能特性

- **多模型接入**：通过 Spring AI Alibaba 接入通义千问；同时提供 LangChain4j、HTTP、SDK 等多种调用方式的 Demo
- **恋爱咨询应用（LoveApp）**
  - 多轮对话记忆（基于文件持久化）
  - 结构化输出（恋爱报告 `LoveReport`）
  - RAG 知识库问答（本地 Markdown 文档 + 阿里云百炼云端知识库）
  - 工具增强对话（联网搜索、网页抓取等）
- **自主 Agent（YuManus）**
  - ReAct（Reasoning + Acting）思考-行动循环
  - 多步工具调用，支持主动终止（`TerminateTool`）
- **内置工具**
  - `WebSearchTool`：通过 SearchAPI 调用百度搜索
  - `WebScrapingTool`：网页内容抓取（Jsoup）
  - `TerminateTool`：终止 Agent 执行
  - `PDFGenerationTool`：生成 PDF 文件（iText）
- **向量检索**：PostgreSQL + pgvector，支持 HNSW 索引
- **MCP 集成**
  - 主项目作为 MCP Client（SSE 连接）
  - 子模块 `image-search-mcp-server` 提供图片搜索 MCP 服务（Pexels API）
- **API 文档**：Knife4j / OpenAPI 3（Swagger UI）

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 / 运行时 | Java 21 |
| 框架 | Spring Boot 3.5.5、Spring AI 1.0.0-M6/M7 |
| 大模型 | 阿里云 DashScope（通义千问 qwen-plus） |
| 向量数据库 | PostgreSQL + pgvector |
| 工具库 | Hutool、Jsoup、iText PDF、Lombok |
| 文档 | Knife4j OpenAPI3 |
| MCP | Spring AI MCP Server / Client |

## 项目结构

```
ai-agent/
├── src/main/java/com/jzy/aiagent/
│   ├── agent/              # Agent 框架（BaseAgent → ReActAgent → ToolCallAgent → YuManus）
│   ├── app/                # LoveApp 恋爱咨询应用
│   ├── tools/              # AI 工具（搜索、抓取、PDF、终止等）
│   ├── rag/                # RAG 相关（文档加载、向量库、查询改写等）
│   ├── config/             # Spring 配置（工具注册、PgVector 等）
│   ├── controller/         # REST 接口
│   ├── advisor/            # ChatClient Advisor（日志、ReReading 等）
│   ├── chatMemory/         # 基于文件的对话记忆
│   ├── demo/               # Spring AI / LangChain4j 调用示例
│   └── entity/             # 实体类（如 LoveReport）
├── src/main/resources/
│   ├── application.yml           # 主配置
│   ├── application-local.yml     # 本地敏感配置（已 gitignore，需自行创建）
│   ├── document/                 # RAG 知识库 Markdown 文档
│   └── mcp-server.json           # MCP stdio 模式服务配置
├── image-search-mcp-server/      # 图片搜索 MCP 服务端（独立 Spring Boot 模块）
└── pom.xml
```

## 环境要求

- JDK 21+
- Maven 3.8+
- PostgreSQL（需安装 [pgvector](https://github.com/pgvector/pgvector) 扩展）
- 以下 API Key（按需配置）：
  - [阿里云 DashScope](https://dashscope.aliyun.com/)（通义千问）
  - [SearchAPI](https://www.searchapi.io/)（百度搜索）
  - [Pexels API](https://www.pexels.com/api/)（图片搜索 MCP 服务）
  - 阿里云百炼知识库（若使用云端 RAG）

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd ai-agent
```

### 2. 配置本地环境

复制并编辑本地配置文件（该文件已在 `.gitignore` 中，不会提交到仓库）：

```yaml
# src/main/resources/application-local.yml
spring:
  ai:
    dashscope:
      api-key: <your-dashscope-api-key>
      chat:
        options:
          model: qwen-plus
  datasource:
    url: jdbc:postgresql://<host>:5432/<database>
    username: <username>
    password: <password>

search-api:
  api-key: <your-searchapi-key>
```

### 3. 启动主应用

```bash
mvn spring-boot:run
```

默认配置：

- 端口：`8123`
- 上下文路径：`/api`
- 健康检查：`GET http://localhost:8123/api/health`
- API 文档：`http://localhost:8123/api/doc.html`

### 4. 启动图片搜索 MCP 服务（可选）

```bash
cd image-search-mcp-server
# 在 application.yml 或环境变量中配置 pexels.api-key
mvn spring-boot:run
```

MCP 服务默认以 **SSE 模式** 运行在 `http://localhost:8127`，主项目 `application.yml` 中已配置对应连接。

如需使用 **stdio 模式**，在 `image-search-mcp-server` 中切换 profile 为 `stdio`，并在主项目启用 `mcp-server.json` 配置。

### 5. 运行测试

```bash
mvn test
```

## Agent 架构

项目采用分层 Agent 设计：

```
BaseAgent          # 状态管理、步骤循环、消息上下文
  └── ReActAgent   # think() + act() 思考-行动模式
        └── ToolCallAgent   # Spring AI 工具调用管理
              └── YuManus   # 通用全能 Agent 实现
```

`YuManus` 会根据用户需求主动选择工具，逐步完成复杂任务，最多执行 20 步，可通过 `terminate` 工具提前结束。

## LoveApp 能力说明

`LoveApp` 是面向恋爱心理咨询场景的示例应用，支持以下调用方式：

| 方法 | 说明 |
|------|------|
| `doChat` | 基础多轮对话（文件持久化记忆） |
| `doChatWithReport` | 结构化输出恋爱报告 |
| `doChatWithRag` | 结合本地知识库与云端知识库的 RAG 问答 |
| `doChatWithTools` | 挂载内置工具 + MCP 工具的增强对话 |

本地知识库文档位于 `src/main/resources/document/`，包含单身、恋爱、已婚三个场景的常见问题与回答。

## 配置说明

### 向量库（pgvector）

`application.yml` 中默认向量维度为 1536，距离度量使用余弦相似度，索引类型为 HNSW。启动时会自动初始化 `vector_store` 表并加载 Markdown 文档。

### MCP Client

主项目通过 SSE 连接外部 MCP 服务：

```yaml
spring:
  ai:
    mcp:
      client:
        sse:
          connections:
            server1:
              url: http://localhost:8127
```

### 对话记忆

多轮对话记忆保存在项目根目录下的 `chat-memory/` 文件夹中。

### 文件输出

工具生成的 PDF 等文件默认保存在 `{项目根目录}/tmp/` 下。

## 子模块：image-search-mcp-server

独立的 Spring Boot 模块，基于 Spring AI MCP Server 暴露 `searchImage` 工具，通过 Pexels API 搜索网络图片。

支持两种运行模式：

- **SSE 模式**（默认）：Web 服务，端口 8127
- **stdio 模式**：标准输入输出，适合 IDE / CLI 集成

配置示例：

```yaml
pexels:
  api-key: <your-pexels-api-key>
  api-url: https://api.pexels.com/v1/search
  per-page: 10
```

## 开发说明

- 本地敏感配置请写入 `application-local.yml`，不要提交到 Git
- 新增 AI 工具：在 `tools/` 包下实现并标注 `@Tool`，然后在 `ToolRegistration` 中注册
- 新增 REST 接口：在 `controller/` 包下添加，Swagger 会自动扫描 `com.jzy.aiagent.controller`
- 项目使用 [OpenSpec](https://github.com/Fission-AI/OpenSpec) 规范驱动开发，配置见 `openspec/config.yaml`

## 许可证

暂未指定开源许可证，使用前请与项目维护者确认。
