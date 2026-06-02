# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: roles.test.ts >> 角色管理功能测试 >> 删除角色
- Location: tests\roles.test.ts:71:3

# Error details

```
Test timeout of 30000ms exceeded.
```

```
Error: locator.click: Test timeout of 30000ms exceeded.
Call log:
  - waiting for locator('aside').locator('text=角色管理')

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
  1  | import { test, expect } from '@playwright/test'
  2  | 
  3  | test.describe('角色管理功能测试', () => {
  4  |   test.beforeEach(async ({ page }) => {
  5  |     await page.goto('/login')
  6  |     await page.waitForSelector('input[placeholder*="用户名"]', { timeout: 10000 })
  7  |     
  8  |     await page.fill('input[placeholder*="用户名"]', 'admin')
  9  |     await page.fill('input[placeholder*="密码"]', 'admin123')
  10 |     
  11 |     const captchaElement = page.locator('div[class*="font-mono"]')
  12 |     const captchaText = await captchaElement.textContent()
  13 |     if (captchaText && captchaText !== '--') {
  14 |       await page.fill('input[placeholder*="验证码"]', captchaText)
  15 |     }
  16 |     
  17 |     await page.click('button[type="submit"]')
  18 |     await page.waitForTimeout(5000)
  19 |   })
  20 | 
  21 |   test('进入角色管理页面', async ({ page }) => {
  22 |     const menuItem = page.locator('aside').locator('text=角色管理')
  23 |     await menuItem.click()
  24 |     await page.waitForURL(/system\/roles/, { timeout: 10000 })
  25 |     
  26 |     await expect(page).toHaveURL(/system\/roles/)
  27 |     await expect(page.locator('h2')).toHaveText('角色管理')
  28 |   })
  29 | 
  30 |   test('添加角色', async ({ page }) => {
  31 |     const menuItem = page.locator('aside').locator('text=角色管理')
  32 |     await menuItem.click()
  33 |     await page.waitForURL(/system\/roles/, { timeout: 10000 })
  34 |     
  35 |     await page.click('button:has-text("新增")')
  36 |     await page.waitForTimeout(1000)
  37 |     
  38 |     await page.fill('input[placeholder*="角色名称"]', '测试角色')
  39 |     await page.fill('input[placeholder*="角色编码"]', 'TEST_ROLE')
  40 |     
  41 |     const saveButton = page.locator('button:has-text("确定")')
  42 |     if (await saveButton.count() > 0) {
  43 |       await saveButton.click({ force: true })
  44 |     }
  45 |   })
  46 | 
  47 |   test('编辑角色', async ({ page }) => {
  48 |     const menuItem = page.locator('aside').locator('text=角色管理')
  49 |     await menuItem.click()
  50 |     await page.waitForURL(/system\/roles/, { timeout: 10000 })
  51 |     await page.waitForSelector('.el-table', { timeout: 10000 })
  52 |     
  53 |     await page.waitForTimeout(500)
  54 |     
  55 |     const editButtons = page.locator('.el-table .el-button--text')
  56 |     if ((await editButtons.count()) >= 2) {
  57 |       const editButton = editButtons.first()
  58 |       await editButton.click({ force: true })
  59 |       
  60 |       await expect(page.locator('.el-dialog__title')).toHaveText('编辑')
  61 |       
  62 |       await page.fill('input[placeholder*="角色名称"]', '更新角色')
  63 |       
  64 |       const saveButton = page.locator('.el-dialog').locator('button:has-text("保存")')
  65 |       await saveButton.click()
  66 |       
  67 |       await expect(page.locator('.el-message--success').first()).toContainText('成功')
  68 |     }
  69 |   })
  70 | 
  71 |   test('删除角色', async ({ page }) => {
  72 |     const menuItem = page.locator('aside').locator('text=角色管理')
> 73 |     await menuItem.click()
     |                    ^ Error: locator.click: Test timeout of 30000ms exceeded.
  74 |     await page.waitForURL(/system\/roles/, { timeout: 10000 })
  75 |     await page.waitForSelector('.el-table', { timeout: 10000 })
  76 |     
  77 |     await page.waitForTimeout(500)
  78 |     
  79 |     const deleteButtons = page.locator('.el-table .el-button--text')
  80 |     const deleteButtonCount = await deleteButtons.count()
  81 |     if (deleteButtonCount > 0) {
  82 |       const deleteButton = deleteButtons.nth(deleteButtonCount - 1)
  83 |       
  84 |       page.on('dialog', async dialog => {
  85 |         await dialog.accept()
  86 |       })
  87 |       
  88 |       await deleteButton.click({ force: true })
  89 |       
  90 |       await expect(page.locator('.el-message--success').first()).toContainText('成功')
  91 |     }
  92 |   })
  93 | })
```