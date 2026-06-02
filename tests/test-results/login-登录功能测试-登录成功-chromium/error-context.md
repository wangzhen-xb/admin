# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: login.test.ts >> 登录功能测试 >> 登录成功
- Location: tests\login.test.ts:41:3

# Error details

```
Test timeout of 30000ms exceeded.
```

```
Error: page.waitForURL: Test timeout of 30000ms exceeded.
=========================== logs ===========================
waiting for navigation to "/" until "load"
============================================================
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
  3   | test.describe('登录功能测试', () => {
  4   |   test('访问登录页面', async ({ page }) => {
  5   |     await page.goto('/login')
  6   |     
  7   |     await page.waitForSelector('h1')
  8   |     await expect(page.locator('h1')).toHaveText('欢迎登录')
  9   |   })
  10  | 
  11  |   test('用户名必填验证', async ({ page }) => {
  12  |     await page.goto('/login')
  13  |     await page.waitForSelector('button[type="submit"]')
  14  |     
  15  |     await page.click('button[type="submit"]')
  16  |     
  17  |     await expect(page.locator('.el-message')).toContainText('用户名')
  18  |   })
  19  | 
  20  |   test('密码必填验证', async ({ page }) => {
  21  |     await page.goto('/login')
  22  |     await page.waitForSelector('input[placeholder*="用户名"]')
  23  |     
  24  |     await page.fill('input[placeholder*="用户名"]', 'testuser')
  25  |     await page.click('button[type="submit"]')
  26  |     
  27  |     await expect(page.locator('.el-message')).toContainText('密码')
  28  |   })
  29  | 
  30  |   test('验证码必填验证', async ({ page }) => {
  31  |     await page.goto('/login')
  32  |     await page.waitForSelector('input[placeholder*="用户名"]')
  33  |     
  34  |     await page.fill('input[placeholder*="用户名"]', 'testuser')
  35  |     await page.fill('input[placeholder*="密码"]', '123456')
  36  |     await page.click('button[type="submit"]')
  37  |     
  38  |     await expect(page.locator('.el-message')).toContainText('验证码')
  39  |   })
  40  | 
  41  |   test('登录成功', async ({ page }) => {
  42  |     await page.goto('/login')
  43  |     await page.waitForSelector('input[placeholder*="用户名"]')
  44  |     
  45  |     await page.fill('input[placeholder*="用户名"]', 'admin')
  46  |     await page.fill('input[placeholder*="密码"]', 'admin123')
  47  |     
  48  |     const captchaElement = page.locator('div[class*="font-mono"]')
  49  |     const captchaText = await captchaElement.textContent()
  50  |     if (captchaText && captchaText !== '--') {
  51  |       await page.fill('input[placeholder*="验证码"]', captchaText)
  52  |     } else {
  53  |       await captchaElement.click()
  54  |       const newCaptcha = await captchaElement.textContent()
  55  |       if (newCaptcha) {
  56  |         await page.fill('input[placeholder*="验证码"]', newCaptcha)
  57  |       }
  58  |     }
  59  |     
  60  |     await page.click('button[type="submit"]')
  61  |     
> 62  |     await page.waitForURL('/')
      |                ^ Error: page.waitForURL: Test timeout of 30000ms exceeded.
  63  |     
  64  |     await expect(page.locator('text=首页')).toBeVisible()
  65  |   })
  66  | 
  67  |   test('登录失败 - 错误密码', async ({ page }) => {
  68  |     await page.goto('/login')
  69  |     await page.waitForSelector('input[placeholder*="用户名"]')
  70  |     
  71  |     await page.fill('input[placeholder*="用户名"]', 'admin')
  72  |     await page.fill('input[placeholder*="密码"]', 'wrongpassword')
  73  |     
  74  |     const captchaElement = page.locator('div[class*="font-mono"]')
  75  |     const captchaText = await captchaElement.textContent()
  76  |     if (captchaText && captchaText !== '--') {
  77  |       await page.fill('input[placeholder*="验证码"]', captchaText)
  78  |     }
  79  |     
  80  |     await page.click('button[type="submit"]')
  81  |     
  82  |     await page.waitForSelector('.el-message--error', { timeout: 5000 })
  83  |     const errorMessages = page.locator('.el-message--error')
  84  |     const count = await errorMessages.count()
  85  |     let found = false
  86  |     for (let i = 0; i < count; i++) {
  87  |       const text = await errorMessages.nth(i).textContent()
  88  |       if (text && text.includes('登录失败')) {
  89  |         found = true
  90  |         break
  91  |       }
  92  |     }
  93  |     expect(found).toBe(true)
  94  |   })
  95  | 
  96  |   test('刷新验证码', async ({ page }) => {
  97  |     await page.goto('/login')
  98  |     await page.waitForSelector('div[class*="font-mono"]')
  99  |     
  100 |     const captchaElement = page.locator('div[class*="font-mono"]')
  101 |     const initialCaptcha = await captchaElement.textContent()
  102 |     
  103 |     await captchaElement.click()
  104 |     
  105 |     const newCaptcha = await captchaElement.textContent()
  106 |     
  107 |     expect(newCaptcha).not.toBe(initialCaptcha)
  108 |   })
  109 | 
  110 |   test('密码显示/隐藏切换', async ({ page }) => {
  111 |     await page.goto('/login')
  112 |     await page.waitForSelector('input[type="password"]')
  113 |     
  114 |     const passwordInput = page.locator('input[type="password"]')
  115 |     
  116 |     await passwordInput.fill('testpassword')
  117 |     
  118 |     const buttons = page.locator('button')
  119 |     const buttonCount = await buttons.count()
  120 |     if (buttonCount >= 3) {
  121 |       const toggleButton = buttons.nth(2)
  122 |       await toggleButton.click({ force: true })
  123 |       
  124 |       await page.waitForTimeout(500)
  125 |       const textInput = page.locator('input[type="text"]')
  126 |       await expect(textInput).toHaveValue('testpassword')
  127 |     }
  128 |   })
  129 | })
```