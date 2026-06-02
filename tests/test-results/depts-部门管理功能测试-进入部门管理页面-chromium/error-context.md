# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: depts.test.ts >> 部门管理功能测试 >> 进入部门管理页面
- Location: tests\depts.test.ts:21:3

# Error details

```
Test timeout of 30000ms exceeded.
```

```
Error: locator.click: Test timeout of 30000ms exceeded.
Call log:
  - waiting for locator('aside').locator('text=部门管理')

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
  3   | test.describe('部门管理功能测试', () => {
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
  18  |     await page.waitForTimeout(5000)
  19  |   })
  20  | 
  21  |   test('进入部门管理页面', async ({ page }) => {
  22  |     const menuItem = page.locator('aside').locator('text=部门管理')
> 23  |     await menuItem.click()
      |                    ^ Error: locator.click: Test timeout of 30000ms exceeded.
  24  |     await page.waitForURL(/system\/depts/, { timeout: 10000 })
  25  |     
  26  |     await expect(page).toHaveURL(/system\/depts/)
  27  |     await expect(page.locator('h2')).toHaveText('部门管理')
  28  |   })
  29  | 
  30  |   test('搜索部门', async ({ page }) => {
  31  |     const menuItem = page.locator('aside').locator('text=部门管理')
  32  |     await menuItem.click()
  33  |     await page.waitForURL(/system\/depts/, { timeout: 10000 })
  34  |     await page.waitForSelector('.el-table', { timeout: 10000 })
  35  |     
  36  |     const searchButton = page.locator('button:has-text("搜索")')
  37  |     await searchButton.click()
  38  |     
  39  |     await page.waitForTimeout(500)
  40  |   })
  41  | 
  42  |   test('添加部门', async ({ page }) => {
  43  |     const menuItem = page.locator('aside').locator('text=部门管理')
  44  |     await menuItem.click()
  45  |     await page.waitForURL(/system\/depts/, { timeout: 10000 })
  46  |     
  47  |     await page.click('button:has-text("新增")')
  48  |     await page.waitForTimeout(1000)
  49  |     
  50  |     await page.fill('input[placeholder*="部门名称"]', '测试部门')
  51  |   })
  52  | 
  53  |   test('编辑部门', async ({ page }) => {
  54  |     const menuItem = page.locator('aside').locator('text=部门管理')
  55  |     await menuItem.click()
  56  |     await page.waitForURL(/system\/depts/, { timeout: 10000 })
  57  |     await page.waitForSelector('.el-table', { timeout: 10000 })
  58  |     
  59  |     await page.waitForTimeout(1000)
  60  |     
  61  |     const editButtons = page.locator('.el-table .el-button--text')
  62  |     const buttonCount = await editButtons.count()
  63  |     if (buttonCount >= 2) {
  64  |       const editButton = editButtons.first()
  65  |       await editButton.click({ force: true })
  66  |       
  67  |       await page.waitForSelector('.el-dialog', { timeout: 10000 })
  68  |       
  69  |       await page.fill('input[placeholder*="部门名称"]', '更新部门')
  70  |       
  71  |       const saveButton = page.locator('.el-dialog').locator('button:has-text("保存")')
  72  |       await saveButton.click()
  73  |       
  74  |       await expect(page.locator('.el-message--success').first()).toContainText('成功')
  75  |     }
  76  |   })
  77  | 
  78  |   test('删除部门', async ({ page }) => {
  79  |     const menuItem = page.locator('aside').locator('text=部门管理')
  80  |     await menuItem.click()
  81  |     await page.waitForURL(/system\/depts/, { timeout: 10000 })
  82  |     await page.waitForSelector('.el-table', { timeout: 10000 })
  83  |     
  84  |     await page.waitForTimeout(500)
  85  |     
  86  |     const deleteButtons = page.locator('.el-table .el-button--text')
  87  |     const deleteButtonCount = await deleteButtons.count()
  88  |     if (deleteButtonCount > 0) {
  89  |       const deleteButton = deleteButtons.nth(deleteButtonCount - 1)
  90  |       
  91  |       page.on('dialog', async dialog => {
  92  |         await dialog.accept()
  93  |       })
  94  |       
  95  |       await deleteButton.click({ force: true })
  96  |       
  97  |       await expect(page.locator('.el-message--success').first()).toContainText('成功')
  98  |     }
  99  |   })
  100 | })
```