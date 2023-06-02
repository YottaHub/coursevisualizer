# Java程序设计课程作业（23春）

本作业由高润钊（202228006010015）个人完成。本项目是一个基于JavaFX的课程表数据分析工具，旨在读取国科大教务系统导出的课程表数据，并进行数据分析和可视化展示。

## 项目结构

### 环境要求

- Java 20
- Maven

### 依赖管理

本项目通过Maven实现依赖管理，依赖向有
- JavaFX：用于构建用户界面
- Apache POI：用于解析Excel格式的课表数据文件

### 文件组织

```bash
.
├── pom.xml
├── src
│   └── main
│       ├── java
│       │   ├── cn.ac.ucas.coursevisualizer
│       │   │ ├── CourseVisualizerApp.java # driver class，包含JavaFX的界面设计和事件处理逻辑
│       │   │ └── ExcelHandler.java # 课程表数据解析类，用于解析课程表数据文件
│       │   └── module-info.java
│       └── resources
│           └── 2023年夏季学期课表.xlsx # 用于演示的示例课表
└── target # 编译文件与jar包
    ├── CourseVisualizer-1.0-SNAPSHOT-jar-with-dependencies.jar
    └── CourseVisualizer-1.0-SNAPSHOT.jar
```

## 功能介绍

### 使用方法

1. 克隆或下载本项目的源代码到本地。

```shell
git clone https://github.com/yottahub/coursevisualizer
```

2. 使用Maven构建项目，并下载依赖项。

3. 打开`src/main/java/cn/ac/ucas/coursevisualizer/CourseVisualizerApp.java`文件，运行main方法。

### 操作指南
1. 点击“选择课程表”按钮选择课程表文件，国科大课程表，例如下文演示所使用的“2023年夏季学期课表.xlsx”可以从[国科大选课系统](https://jwxk.ucas.ac.cn/course/termSchedule)中导出（演示文件存放于`src/ersources`文件夹中，如项目结构中所展示）

    ![img0](./images/Screenshot%202023-06-03%20at%2004.12.33.png)

2. 文件识别成功后（识别失败会出现红色文字提醒），会出现新按钮，首先请点击下拉分类，本项目提供“课程属性”和“开课院系”两个选项

3. 分类选择后，点击统计图类型（柱状图或饼状图）按钮即可对学期课表进行可视化分析

### 功能介绍

#### 主界面

主界面含有**操作指引**，**logo图片**和**文件选择按钮**三个元素。

![img1](./images/Screenshot%202023-06-03%20at%2003.55.05.png)

#### 文件选择

**文件选择**功能支持文件过滤（选择过程中默认只能选择Excel文件和目录）。

```java
fileChooser.getExtensionFilters().addAll(  
    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"),  
    new FileChooser.ExtensionFilter("All Files", "*.*"));
```

**文件选择**功能支持课表文件识别，当识别成功时才会出现可视化模块按钮。

```java
// do sanity check on the first row  
if (row.getRowNum() == 0) {  
    // sanity check  
    if (sanityCheck(row)) {  
        isInitialized = true;  
        continue;  
    } else {  
        break;      
}  
}
```

![img2](./images/Screenshot%202023-06-03%20at%2003.26.23.png)

当识别失败时，后端打印错误原因并在窗口警告文件格式错误。

![img3](./images/Screenshot%202023-06-03%20at%2003.27.15.png)

![img4](./images/Screenshot%202023-06-03%20at%2003.27.49.png)

#### 可视化分析

项目支持对课表中**开课院系**和**课程属性**两列进行数据分析和可视化展示。

对**开课院系**进行**柱状图**可视化结果

![img7](./images/Screenshot%202023-06-03%20at%2003.35.26.png)

对**开课院系**进行**饼状图**可视化结果

![img8](./images/Screenshot%202023-06-03%20at%2003.35.40.png)

对**课程属性**进行**柱状图**可视化结果

![img6](./images/Screenshot%202023-06-03%20at%2003.34.11.png)

对**课程属性**进行**饼状图**可视化结果

![img5](.images/../images/Screenshot%202023-06-03%20at%2003.35.03.png)

_感谢您使用本课表数据可视化工具！_
