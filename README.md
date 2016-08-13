# OkHttpTaskUtils
okhttp封装的网络请求
仅用于自己的项目，其他人请先阅读源码后再尝试。 <br />
初始化配置：<br />
 OkHttpTask.getInstance().initDebugModel(BuildConfig.ISDEBUG);


用法: <br />
* gradle: <br />
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
Step 2. Add the dependency

```
	dependencies {
	        compile 'com.github.jiang111:OkHttpTaskUtils:2.1'
	}
```
