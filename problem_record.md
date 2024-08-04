记录开发过程中的问题


## iOS

### 1. Configurations 中提示找不到 XCode 环境

1. 下载 XCode
2. 执行 `sudo xcode-select reset`
3. 可以安装 `kdoctor` 检查环境 `brew install kdoctor`

### 2. Configurations 中提示 `Error: Selected device not found`

各种软件版本问题，升级 macOS 及 XCode 到最新版可解决

参考[youtrack-KT-61624]


### 3. 编译报错 `compileKotlinIosSimulatorArm64 FAILED error: Compilation failed: No such value argument slot in IrCallImpl`

commonMain 中的接口没有在 iosMain 中写上对应的实现，补全即可

### 4. iOS APP 没有全屏显示

iOS 默认会让 APP 限制在安全区域内，而模版[ContentView.swift]文件中仅对`.keyboard`做了忽略






youtrack-KT-61624: https://youtrack.jetbrains.com/issue/KT-61624
ContentView.swift: ./iosApp/iosApp/ContentView.swift