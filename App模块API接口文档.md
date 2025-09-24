# App 模块 API 接口文档

## 概述

本文档描述了 App 模块的所有 API 接口，包括用户功能和管理员功能。

## 用户功能接口

### 1. 创建应用
- **接口路径**: `POST /app/add`
- **功能描述**: 用户创建应用，须填写 initPrompt
- **请求参数**: `AppAddRequest`
  ```json
  {
    "appName": "应用名称",
    "initPrompt": "应用初始化prompt（必填）",
    "codeGenType": "代码生成类型"
  }
  ```
- **返回结果**: `BaseResponse<Long>` - 返回新创建应用的ID
- **权限要求**: 需要登录

### 2. 修改自己的应用
- **接口路径**: `POST /app/update`
- **功能描述**: 根据 id 修改自己的应用（目前只支持修改应用名称）
- **请求参数**: `AppUpdateRequest`
  ```json
  {
    "id": 1,
    "appName": "新的应用名称"
  }
  ```
- **返回结果**: `BaseResponse<Boolean>`
- **权限要求**: 需要登录，只能修改自己的应用

### 3. 删除自己的应用
- **接口路径**: `POST /app/delete`
- **功能描述**: 根据 id 删除自己的应用
- **请求参数**: `DeleteRequest`
  ```json
  {
    "id": 1
  }
  ```
- **返回结果**: `BaseResponse<Boolean>`
- **权限要求**: 需要登录，只能删除自己的应用

### 4. 查看应用详情
- **接口路径**: `GET /app/get/vo?id={id}`
- **功能描述**: 根据 id 查看应用详情
- **请求参数**: `id` (Long) - 应用ID
- **返回结果**: `BaseResponse<AppVO>` - 包含应用详细信息和创建者信息
- **权限要求**: 无

### 5. 分页查询自己的应用列表
- **接口路径**: `POST /app/my/list/page/vo`
- **功能描述**: 分页查询自己的应用列表（支持根据名称查询，每页最多 20 个）
- **请求参数**: `AppQueryRequest`
  ```json
  {
    "pageNum": 1,
    "pageSize": 10,
    "appName": "搜索关键词",
    "sortField": "createTime",
    "sortOrder": "descend"
  }
  ```
- **返回结果**: `BaseResponse<Page<AppVO>>`
- **权限要求**: 需要登录

### 6. 分页查询精选应用列表
- **接口路径**: `POST /app/list/page/vo/featured`
- **功能描述**: 分页查询精选的应用列表（支持根据名称查询，每页最多 20 个）
- **请求参数**: `AppQueryRequest`
- **返回结果**: `BaseResponse<Page<AppVO>>`
- **权限要求**: 无

## 管理员功能接口

### 1. 删除任意应用
- **接口路径**: `POST /app/delete/admin`
- **功能描述**: 管理员根据 id 删除任意应用
- **请求参数**: `DeleteRequest`
- **返回结果**: `BaseResponse<Boolean>`
- **权限要求**: 管理员权限

### 2. 更新任意应用
- **接口路径**: `POST /app/update/admin`
- **功能描述**: 管理员根据 id 更新任意应用（支持更新应用名称、应用封面、优先级）
- **请求参数**: `AppAdminUpdateRequest`
  ```json
  {
    "id": 1,
    "appName": "应用名称",
    "cover": "应用封面URL",
    "priority": 10
  }
  ```
- **返回结果**: `BaseResponse<Boolean>`
- **权限要求**: 管理员权限

### 3. 分页查询应用列表
- **接口路径**: `POST /app/list/page/vo/admin`
- **功能描述**: 管理员分页查询应用列表（支持根据除时间外的任何字段查询，每页数量不限）
- **请求参数**: `AppQueryRequest`
  ```json
  {
    "pageNum": 1,
    "pageSize": 50,
    "appName": "应用名称",
    "userId": 123,
    "codeGenType": "html",
    "priority": 10,
    "sortField": "priority",
    "sortOrder": "descend"
  }
  ```
- **返回结果**: `BaseResponse<Page<AppVO>>`
- **权限要求**: 管理员权限

### 4. 查看应用详情
- **接口路径**: `GET /app/get/admin?id={id}`
- **功能描述**: 管理员根据 id 查看应用详情（返回完整的 App 实体）
- **请求参数**: `id` (Long) - 应用ID
- **返回结果**: `BaseResponse<App>`
- **权限要求**: 管理员权限

## 数据模型

### AppVO (应用视图对象)
```json
{
  "id": 1,
  "appName": "应用名称",
  "cover": "应用封面URL",
  "initPrompt": "初始化prompt",
  "codeGenType": "代码生成类型",
  "deployKey": "部署标识",
  "deployedTime": "2024-01-01T00:00:00",
  "priority": 10,
  "userId": 123,
  "user": {
    "id": 123,
    "userName": "用户名",
    "userAvatar": "头像URL"
  },
  "createTime": "2024-01-01T00:00:00",
  "updateTime": "2024-01-01T00:00:00"
}
```

## 特殊说明

1. **精选应用规则**: 优先级不为空且大于0的应用会被认为是精选应用，按优先级降序排列
2. **权限控制**: 
   - 用户只能修改和删除自己创建的应用
   - 管理员可以操作所有应用
3. **分页限制**: 
   - 用户查询自己的应用和精选应用时，每页最多20个
   - 管理员查询时无限制
4. **参数校验**:
   - 创建应用时 initPrompt 为必填项
   - 应用名称最长80字符
   - initPrompt 最长8192字符

## 错误码说明

- `40000`: 请求参数错误
- `40001`: 请求数据不存在
- `40101`: 未登录
- `40301`: 无权限
- `50000`: 系统内部异常
- `50001`: 操作失败