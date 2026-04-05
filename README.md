# RepoSample - WanAndroid Clean Architecture

这是一个基于 **Clean Architecture (洁净架构)** 实现的 Android 示例项目。它展示了如何结合现代 Android 开发组件（Jetpack Compose, Coroutines, Flow, Koin, Retrofit, Room）实现一个具备 **离线缓存能力** 的应用。

## 🚀 核心特性

- **Clean Architecture**: 严格划分 `data`, `domain`, `ui` 层，确保业务逻辑与技术实现解耦。
- **Offline-First (离线优先)**: 仓库层采用“优先读取缓存 -> 网络请求更新 -> 同步本地数据库”的策略。
- **响应式编程**: 全链路使用 Kotlin `Flow` 处理数据流。
- **依赖注入**: 使用 `Koin` 实现轻量级的依赖管理。
- **安全请求**: 封装 `safeApiCall` 统一处理网络异常及业务错误（errorCode != 0）。
- **现代 UI**: 使用 `Jetpack Compose` 构建声明式 UI 界面。

## 🛠️ 技术栈

- **语言**: [Kotlin](https://kotlinlang.org/)
- **异步**: [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html)
- **依赖注入**: [Koin](https://insert-koin.io/)
- **网络**: [Retrofit 2](https://square.github.io/retrofit/) + OkHttp
- **数据库**: [Room](https://developer.android.com/training/data-storage/room) (SQLite)
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **架构**: MVI + Clean Architecture + UseCases

## 📂 项目结构

```text
com.sample.reposample
├── data (数据层)
│   ├── local          # Room 数据库、DAO 及 Entity
│   ├── remote         # Retrofit 接口、DTO 及 SafeApiCall 工具
│   └── repository     # Repository 接口的实现 (SSOT 逻辑)
├── domain (领域层)
│   ├── model          # 业务模型 (Domain Model)
│   ├── repository     # Repository 接口定义
│   └── usecase        # 具体的业务用例 (GetArticlesUseCase)
├── ui (表现层)
│   ├── ArticleViewModel.kt  # 状态管理
│   └── theme          # Compose 主题配置
└── util (工具层)
    └── Resource.kt    # 统一的状态包装类 (Loading/Success/Error)
```

## 📝 核心设计思路

### 1. 缓存优先策略 (Repository Implementation)
在 `ArticleRepositoryImpl` 中，我们通过 `Flow` 多次发射数据：
1. `emit(Resource.Loading(cache))`：立即返回数据库中的旧数据，实现“秒开”。
2. 网络请求成功后，通过 `dao.insertArticles()` 同步最新数据。
3. `emit(Resource.Success(latestData))`：返回最新数据并更新 UI。

### 2. 异常处理
使用自定义的 `safeApiCall` 捕获所有潜在的 `IOException` 和 `HttpException`。即使网络失败，应用依然会通过 `Resource.Error(message, cache)` 将最后一次成功的缓存展示给用户。

## ⚙️ 如何运行

1. 克隆项目。
2. 确保 Android Studio 升级到最新版 (支持 Kotlin 2.0+)。
3. 直接运行 `app` 模块。
4. 本项目使用的 API 来源于 [WanAndroid](https://www.wanandroid.com/) 开放平台。

---

**Happy Coding!** 🚀
