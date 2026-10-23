# Aerial 极米定制版

基于 [上游 Aerial](https://github.com/pengchujin/aerial-tv) v1.38，长期分支为 `xgimi-custom`。本仓库保留上游历史和 MIT 许可；定制改动使用通用署名，本机路径、设备地址和签名材料不纳入公开内容。

## 当前版本与验证

- 定制版：`1.38-av2`，`versionCode=43`，上游基线 `039b267`。
- 修复桌面预览与系统屏保同时自动启动：当系统已启用 Aerial Views 时，由系统负责自动进入，桌面保留手动预览。
- 屏保等待时间同步到系统；“从不”关闭屏保，重新选择时长后恢复。
- NEW Z6X / Android 8 实测：闲置 5 分钟后自动进入正式屏保，观察 5 分多钟未自行退出，画面正常切换；按键退出和手动预览正常。
- 发布构建与 Lint Vital 通过；2 分钟、“从不”和恢复 5 分钟设置通过。未进行整机重启或数小时稳定性验证，不代表所有型号均已验证。
- 完整 Lint 仍存在上游问题，不声称全量检查通过。

## 联动与更新

需要另行安装 [Aerial Views](https://github.com/theothernt/AerialViews)。配置入口、电视界面和手机页均支持选择 Aerial Views；应用不存在时保留回退行为。

版本名保留 `-av` 标记，避免用上游不同签名的 APK 直接更新。自建 APK 必须保持自己的签名；上游发布包和其他构建者的 APK 不能视为可覆盖安装的同签名更新。

## 本地构建

在任意工作目录检出源码，使用 JDK 17、Android SDK 36 与项目 Gradle Wrapper。SDK 路径放在忽略的 `local.properties` 中；签名密钥与签名属性放在忽略的 `local/` 中，格式参见 `app/build.gradle.kts`。没有自己的发布签名时，不要覆盖已有不同签名的安装。

```powershell
.\gradlew.bat assembleStandardRelease lintVitalStandardRelease
```

产物位于 `app/build/outputs/apk/standard/release/`。本公开仓库只发布源码，不包含个人签名密钥、设备资料、现场截图、私有日志或本机构建脚本。

## 合并上游

将作者仓库设为 `upstream`，将自己的仓库设为 `origin`。以下 `X.Y` 替换成实际版本：

```powershell
git switch xgimi-custom
git status --short
git fetch upstream tag vX.Y
git switch -c update/xgimi-vX.Y
git merge --no-ff --no-commit vX.Y
```

工作区有修改时先保存。逐项处理冲突，重点核对 `MainActivity`、`GuardSetup`、`Wallpapers`、屏保界面和 `Updates`，不能整文件覆盖定制联动。版本号应高于设备已安装版本和新上游版本，应用包名与自己的签名保持一致。

发布构建、检查签名并完成目标设备验证后，再推进长期分支和版本标签。设备连接地址用环境变量或私有配置传入，不写入公开源码；临时测试参数恢复原值。

## 私有资料与恢复

`local/`、`local.properties`、签名密钥、密码及设备配置必须保持忽略，不使用 `git add -f` 强行加入。公开前检查提交内容和历史，避免把个人恢复记录提交到公开分支。

Git 标签可以恢复源码；Android 通常拒绝覆盖安装较低版本号的正式 APK。恢复旧代码时使用原密钥和更高版本号重新构建，不通过卸载绕过签名检查，以免丢失数据。
