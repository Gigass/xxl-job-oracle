# XXL-Job Oracle

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

## 简介

`xxl-job-oracle` 是一个基于著名开源项目 [xxl-job](https://github.com/xuxueli/xxl-job) 的修改版，旨在为使用 Oracle 数据库的用户提供开箱即用的支持。

原版 `xxl-job` 是一个轻量级分布式任务调度平台，其核心设计目标是开发迅速、学习简单、轻量级、易于扩展。`xxl-job-oracle` 继承了 `xxl-job` 的所有优秀特性，并通过修改数据访问层，使其能够无缝地在 Oracle 数据库上运行。

## 特性

- **简单**: 支持通过Web页面对任务进行CRUD操作，操作简单，一分钟上手。
- **动态**: 支持动态修改任务状态、启动/停止任务，以及终止运行中任务，即时生效。
- **调度中心HA（中心式）**: "调度中心"基于集群部署，可保证调度中心HA。
- **执行器HA（分布式）**: "执行器"支持集群部署，可保证任务执行HA。
- **注册中心**: 执行器会周期性自动注册任务, 调度中心将会自动发现注册的任务并触发执行。
- **弹性扩容缩容**: 一旦有新执行器机器上线或者下线，下次调度时将会重新分配任务。
- **路由策略**: 执行器集群部署时提供丰富的路由策略，包括：第一个、最后一个、轮询、随机、一致性HASH、最不经常使用、最近最久未使用、故障转移、忙碌转移等。
- **故障转移**: 任务路由策略选择"故障转移"情况下，如果执行器集群中某一台机器故障，将会自动Failover切换到一台正常的执行器发送调度请求。
- **多种任务模式**: 支持Bean模式、GLUE模式（Java、Shell、Python、NodeJS、PHP、PowerShell）等多种任务模式。
- **邮件告警**: 任务失败时支持邮件告警，支持配置多邮件地址群发报警邮件。

## 快速入门

### 1. 数据库初始化

请找到项目中的 `doc/db/tables_xxl_job-oracle.sql` 文件，并在您的 Oracle 数据库中执行它，以初始化任务调度中心所需的表结构。

### 2. 配置并部署调度中心 (xxl-job-admin)

调度中心是任务管理和调度的核心。

- **配置文件路径**: `xxl-job-admin/src/main/resources/application.properties`
- **修改数据库配置**: 请根据您自己的环境，修改以下 Oracle 数据库连接信息。

```properties
spring.datasource.url=jdbc:oracle:thin:@//127.0.0.1:1521/XE
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

配置完成后，您可以直接运行 `XxlJobAdminApplication.java` 或通过 Maven 打包后启动。

### 3. 编译项目

在项目根目录下，执行以下 Maven 命令进行编译打包：

```bash
mvn clean package
```

### 4. 配置并部署执行器

执行器是任务的实际执行者。您可以参考 `xxl-job-executor-samples` 目录下的示例项目。以 `xxl-job-executor-sample-springboot` 为例：

- **配置文件路径**: `xxl-job-executor-sample-springboot/src/main/resources/application.properties`
- **修改配置**:
  ```properties
  # 调度中心部署跟地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册
  xxl.job.admin.addresses=http://127.0.0.1:8080/xxl-job-admin
  
  # 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
  xxl.job.executor.appname=xxl-job-executor-sample
  
  # 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999
  xxl.job.executor.port=9999
  ```

## 项目结构

```
xxl-job-oracle/
├── doc/                        -- 文档和SQL脚本
├── xxl-job-admin/              -- 调度中心
├── xxl-job-core/               -- 核心依赖 (RPC、序列化等)
└── xxl-job-executor-samples/   -- 执行器示例项目
```

## 文档

本项目主要修改了数据库适配层，核心功能和概念与原版 `xxl-job` 完全一致。因此，您可以直接参考 [XXL-JOB 官方文档](https://www.xuxueli.com/xxl-job/)。

## 许可证

本项目基于 [MIT License](https://opensource.org/licenses/MIT) 开源。
