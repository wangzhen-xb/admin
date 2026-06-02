import { test, expect } from '@playwright/test'

test.describe('登录功能测试', () => {
  test('访问登录页面', async ({ page }) => {
    await page.goto('/login')
    
    await page.waitForSelector('h1')
    await expect(page.locator('h1')).toHaveText('欢迎登录')
  })

  test('用户名必填验证', async ({ page }) => {
    await page.goto('/login')
    await page.waitForSelector('button[type="submit"]')
    
    await page.click('button[type="submit"]')
    
    await expect(page.locator('.el-message')).toContainText('用户名')
  })

  test('密码必填验证', async ({ page }) => {
    await page.goto('/login')
    await page.waitForSelector('input[placeholder*="用户名"]')
    
    await page.fill('input[placeholder*="用户名"]', 'testuser')
    await page.click('button[type="submit"]')
    
    await expect(page.locator('.el-message')).toContainText('密码')
  })

  test('验证码必填验证', async ({ page }) => {
    await page.goto('/login')
    await page.waitForSelector('input[placeholder*="用户名"]')
    
    await page.fill('input[placeholder*="用户名"]', 'testuser')
    await page.fill('input[placeholder*="密码"]', '123456')
    await page.click('button[type="submit"]')
    
    await expect(page.locator('.el-message')).toContainText('验证码')
  })

  test('登录成功', async ({ page }) => {
    await page.goto('/login')
    await page.waitForSelector('input[placeholder*="用户名"]')
    
    await page.fill('input[placeholder*="用户名"]', 'admin')
    await page.fill('input[placeholder*="密码"]', 'admin123')
    
    const captchaElement = page.locator('div[class*="font-mono"]')
    const captchaText = await captchaElement.textContent()
    if (captchaText && captchaText !== '--') {
      await page.fill('input[placeholder*="验证码"]', captchaText)
    } else {
      await captchaElement.click()
      const newCaptcha = await captchaElement.textContent()
      if (newCaptcha) {
        await page.fill('input[placeholder*="验证码"]', newCaptcha)
      }
    }
    
    await page.click('button[type="submit"]')
    
    await page.waitForURL('/')
    
    await expect(page.locator('text=首页')).toBeVisible()
  })

  test('登录失败 - 错误密码', async ({ page }) => {
    await page.goto('/login')
    await page.waitForSelector('input[placeholder*="用户名"]')
    
    await page.fill('input[placeholder*="用户名"]', 'admin')
    await page.fill('input[placeholder*="密码"]', 'wrongpassword')
    
    const captchaElement = page.locator('div[class*="font-mono"]')
    const captchaText = await captchaElement.textContent()
    if (captchaText && captchaText !== '--') {
      await page.fill('input[placeholder*="验证码"]', captchaText)
    }
    
    await page.click('button[type="submit"]')
    
    await page.waitForSelector('.el-message--error', { timeout: 5000 })
    const errorMessages = page.locator('.el-message--error')
    const count = await errorMessages.count()
    let found = false
    for (let i = 0; i < count; i++) {
      const text = await errorMessages.nth(i).textContent()
      if (text && text.includes('登录失败')) {
        found = true
        break
      }
    }
    expect(found).toBe(true)
  })

  test('刷新验证码', async ({ page }) => {
    await page.goto('/login')
    await page.waitForSelector('div[class*="font-mono"]')
    
    const captchaElement = page.locator('div[class*="font-mono"]')
    const initialCaptcha = await captchaElement.textContent()
    
    await captchaElement.click()
    
    const newCaptcha = await captchaElement.textContent()
    
    expect(newCaptcha).not.toBe(initialCaptcha)
  })

  test('密码显示/隐藏切换', async ({ page }) => {
    await page.goto('/login')
    await page.waitForSelector('input[type="password"]')
    
    const passwordInput = page.locator('input[type="password"]')
    
    await passwordInput.fill('testpassword')
    
    const buttons = page.locator('button')
    const buttonCount = await buttons.count()
    if (buttonCount >= 3) {
      const toggleButton = buttons.nth(2)
      await toggleButton.click({ force: true })
      
      await page.waitForTimeout(500)
      const textInput = page.locator('input[type="text"]')
      await expect(textInput).toHaveValue('testpassword')
    }
  })
})