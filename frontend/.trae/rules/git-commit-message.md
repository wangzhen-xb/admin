---
alwaysApply: true
scene: git_message
---

在此处编写规则，自定义 AI 生成提交信息的风格。
// 遵循 Conventional Commits 规范生成提交信息
// 格式：<type>(<scope>): <subject>
// 
// type 类型说明：
// - feat: 新功能
// - fix: 修复 bug
// - docs: 文档更新
// - style: 代码格式调整（不影响功能）
// - refactor: 重构代码
// - perf: 性能优化
// - test: 添加或修改测试
// - chore: 构建过程或辅助工具的变动
// - ci: CI 配置修改
// - revert: 回滚提交
//
// 规则要求：
// 1. subject 使用中文描述，简洁明了，不超过 50 个字符
// 2. 使用动词开头，如：添加、修复、更新、优化、重构、删除
// 3. 结尾不加句号
// 4. scope 可选，用于标识影响范围（如模块名、组件名）
// 5. 正文部分（如有）详细说明改动原因和与上一版本的差异
// 6. 破坏性变更需在类型后加 ! 并在正文中说明
//
// 示例：
// feat(user): 添加用户登录功能
// fix(api): 修复接口返回空数据问题
// refactor(utils): 优化日期格式化工具


