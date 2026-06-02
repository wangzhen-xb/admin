# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: users.test.ts >> 用户管理功能测试 >> 分页功能
- Location: tests\users.test.ts:131:3

# Error details

```
Test timeout of 30000ms exceeded while running "beforeEach" hook.
```

```
Error: page.waitForSelector: Test timeout of 30000ms exceeded.
Call log:
  - waiting for locator('text=首页') to be visible

```

# Page snapshot

```yaml
- generic [ref=e10]:
  - generic [ref=e11]:
    - img [ref=e13]
    - heading "欢迎登录" [level=1] [ref=e16]
    - paragraph [ref=e17]: 管理系统
  - generic [ref=e18]:
    - generic [ref=e20]:
      - img [ref=e23]
      - textbox "用户名" [ref=e26]: admin
    - generic [ref=e28]:
      - img [ref=e31]
      - textbox "密码" [ref=e34]: admin123
      - img [ref=e37]
    - generic [ref=e42]:
      - generic [ref=e44]:
        - img [ref=e47]
        - textbox "验证码" [ref=e49]
      - generic [ref=e51] [cursor=pointer]: "--"
    - button "登 录" [active] [ref=e52] [cursor=pointer]:
      - generic [ref=e53]: 登 录
  - paragraph [ref=e55]: "用户名: admin"
```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test'
  2   | 
  3   | test.describe('用户管理功能测试', () => {
  4   |   test.beforeEach(async ({ page }) => {
  5   |     await page.goto('/login')
  6   |     await page.waitForSelector('input[placeholder*="用户名"]', { timeout: 10000 })
  7   |     
  8   |     await page.fill('input[placeholder*="用户名"]', 'admin')
  9   |     await page.fill('input[placeholder*="密码"]', 'admin123')
  10  |     
  11  |     const captchaElement = page.locator('div[class*="font-mono"]')
  12  |     const captchaText = await captchaElement.textContent()
  13  |     if (captchaText && captchaText !== '--') {
  14  |       await page.fill('input[placeholder*="验证码"]', captchaText)
  15  |     }
  16  |     
  17  |     await page.click('button[type="submit"]')
  18  |     try {
  19  |       await page.waitForURL('**/', { timeout: 20000 })
  20  |     } catch {
  21  |       await page.waitForTimeout(5000)
  22  |     }
> 23  |     await page.waitForSelector('text=首页', { timeout: 15000 })
      |                ^ Error: page.waitForSelector: Test timeout of 30000ms exceeded.
  24  |   })
  25  | 
  26  |   test('进入用户管理页面', async ({ page }) => {
  27  |     const menuItem = page.locator('aside').locator('text=用户管理')
  28  |     await menuItem.click()
  29  |     await page.waitForURL(/system\/users/, { timeout: 10000 })
  30  |     
  31  |     await expect(page).toHaveURL(/system\/users/)
  32  |     await expect(page.locator('h2')).toHaveText('用户管理')
  33  |   })
  34  | 
  35  |   test('搜索用户', async ({ page }) => {
  36  |     const menuItem = page.locator('aside').locator('text=用户管理')
  37  |     await menuItem.click()
  38  |     await page.waitForURL(/system\/users/, { timeout: 10000 })
  39  |     await page.waitForSelector('.el-table', { timeout: 10000 })
  40  |     
  41  |     await page.waitForTimeout(1000)
  42  |     
  43  |     const searchButton = page.locator('button:has-text("搜索")')
  44  |     await searchButton.click()
  45  |     
  46  |     await page.waitForTimeout(500)
  47  |   })
  48  | 
  49  |   test('重置搜索条件', async ({ page }) => {
  50  |     const menuItem = page.locator('aside').locator('text=用户管理')
  51  |     await menuItem.click()
  52  |     await page.waitForURL(/system\/users/, { timeout: 10000 })
  53  |     
  54  |     await page.fill('input[placeholder*="用户名"]', 'test')
  55  |     await page.click('button:has-text("重置")')
  56  |     
  57  |     const inputValue = await page.inputValue('input[placeholder*="用户名"]')
  58  |     expect(inputValue).toBe('')
  59  |   })
  60  | 
  61  |   test('添加用户', async ({ page }) => {
  62  |     const menuItem = page.locator('aside').locator('text=用户管理')
  63  |     await menuItem.click()
  64  |     await page.waitForURL(/system\/users/, { timeout: 10000 })
  65  |     
  66  |     await page.click('button:has-text("新增")')
  67  |     await page.waitForSelector('.el-dialog', { timeout: 10000 })
  68  |     
  69  |     await expect(page.locator('.el-dialog__title')).toHaveText('新增')
  70  |     
  71  |     await page.fill('input[placeholder*="用户名"]', 'testuser_new')
  72  |     await page.fill('input[placeholder*="密码"]', '123456')
  73  |     await page.fill('input[placeholder*="邮箱"]', 'test@example.com')
  74  |     await page.fill('input[placeholder*="电话"]', '13800138000')
  75  |     
  76  |     const saveButton = page.locator('.el-dialog').locator('button:has-text("保存")')
  77  |     await saveButton.click()
  78  |     
  79  |     await expect(page.locator('.el-message--success').first()).toContainText('成功')
  80  |   })
  81  | 
  82  |   test('编辑用户', async ({ page }) => {
  83  |     const menuItem = page.locator('aside').locator('text=用户管理')
  84  |     await menuItem.click()
  85  |     await page.waitForURL(/system\/users/, { timeout: 10000 })
  86  |     await page.waitForSelector('.el-table', { timeout: 10000 })
  87  |     
  88  |     await page.waitForTimeout(1500)
  89  |     
  90  |     const editButtons = page.locator('.el-table .el-button--text')
  91  |     const buttonCount = await editButtons.count()
  92  |     if (buttonCount >= 2) {
  93  |       const editButton = editButtons.nth(1)
  94  |       await editButton.click({ force: true })
  95  |       
  96  |       const dialog = page.locator('.el-dialog')
  97  |       if (await dialog.count() > 0) {
  98  |         await page.fill('input[placeholder*="邮箱"]', 'updated@example.com')
  99  |         
  100 |         const saveButton = page.locator('.el-dialog button:has-text("保存")')
  101 |         await saveButton.click({ force: true })
  102 |         
  103 |         await expect(page.locator('.el-message--success').first()).toContainText('成功')
  104 |       }
  105 |     }
  106 |   })
  107 | 
  108 |   test('删除用户', async ({ page }) => {
  109 |     const menuItem = page.locator('aside').locator('text=用户管理')
  110 |     await menuItem.click()
  111 |     await page.waitForURL(/system\/users/, { timeout: 10000 })
  112 |     await page.waitForSelector('.el-table', { timeout: 10000 })
  113 |     
  114 |     await page.waitForTimeout(500)
  115 |     
  116 |     const deleteButtons = page.locator('.el-table .el-button--text')
  117 |     const deleteButtonCount = await deleteButtons.count()
  118 |     if (deleteButtonCount > 0) {
  119 |       const deleteButton = deleteButtons.nth(deleteButtonCount - 1)
  120 |       
  121 |       page.on('dialog', async dialog => {
  122 |         await dialog.accept()
  123 |       })
```