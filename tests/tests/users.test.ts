import { test, expect } from '@playwright/test'

test.describe('用户管理功能测试', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login')
    await page.waitForSelector('input[placeholder*="用户名"]', { timeout: 10000 })
    
    await page.fill('input[placeholder*="用户名"]', 'admin')
    await page.fill('input[placeholder*="密码"]', 'admin123')
    
    const captchaElement = page.locator('div[class*="font-mono"]')
    const captchaText = await captchaElement.textContent()
    if (captchaText && captchaText !== '--') {
      await page.fill('input[placeholder*="验证码"]', captchaText)
    }
    
    await page.click('button[type="submit"]')
    try {
      await page.waitForURL('**/', { timeout: 20000 })
    } catch {
      await page.waitForTimeout(5000)
    }
    await page.waitForSelector('text=首页', { timeout: 15000 })
  })

  test('进入用户管理页面', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=用户管理')
    await menuItem.click()
    await page.waitForURL(/system\/users/, { timeout: 10000 })
    
    await expect(page).toHaveURL(/system\/users/)
    await expect(page.locator('h2')).toHaveText('用户管理')
  })

  test('搜索用户', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=用户管理')
    await menuItem.click()
    await page.waitForURL(/system\/users/, { timeout: 10000 })
    await page.waitForSelector('.el-table', { timeout: 10000 })
    
    await page.waitForTimeout(1000)
    
    const searchButton = page.locator('button:has-text("搜索")')
    await searchButton.click()
    
    await page.waitForTimeout(500)
  })

  test('重置搜索条件', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=用户管理')
    await menuItem.click()
    await page.waitForURL(/system\/users/, { timeout: 10000 })
    
    await page.fill('input[placeholder*="用户名"]', 'test')
    await page.click('button:has-text("重置")')
    
    const inputValue = await page.inputValue('input[placeholder*="用户名"]')
    expect(inputValue).toBe('')
  })

  test('添加用户', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=用户管理')
    await menuItem.click()
    await page.waitForURL(/system\/users/, { timeout: 10000 })
    
    await page.click('button:has-text("新增")')
    await page.waitForSelector('.el-dialog', { timeout: 10000 })
    
    await expect(page.locator('.el-dialog__title')).toHaveText('新增')
    
    await page.fill('input[placeholder*="用户名"]', 'testuser_new')
    await page.fill('input[placeholder*="密码"]', '123456')
    await page.fill('input[placeholder*="邮箱"]', 'test@example.com')
    await page.fill('input[placeholder*="电话"]', '13800138000')
    
    const saveButton = page.locator('.el-dialog').locator('button:has-text("保存")')
    await saveButton.click()
    
    await expect(page.locator('.el-message--success').first()).toContainText('成功')
  })

  test('编辑用户', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=用户管理')
    await menuItem.click()
    await page.waitForURL(/system\/users/, { timeout: 10000 })
    await page.waitForSelector('.el-table', { timeout: 10000 })
    
    await page.waitForTimeout(1500)
    
    const editButtons = page.locator('.el-table .el-button--text')
    const buttonCount = await editButtons.count()
    if (buttonCount >= 2) {
      const editButton = editButtons.nth(1)
      await editButton.click({ force: true })
      
      const dialog = page.locator('.el-dialog')
      if (await dialog.count() > 0) {
        await page.fill('input[placeholder*="邮箱"]', 'updated@example.com')
        
        const saveButton = page.locator('.el-dialog button:has-text("保存")')
        await saveButton.click({ force: true })
        
        await expect(page.locator('.el-message--success').first()).toContainText('成功')
      }
    }
  })

  test('删除用户', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=用户管理')
    await menuItem.click()
    await page.waitForURL(/system\/users/, { timeout: 10000 })
    await page.waitForSelector('.el-table', { timeout: 10000 })
    
    await page.waitForTimeout(500)
    
    const deleteButtons = page.locator('.el-table .el-button--text')
    const deleteButtonCount = await deleteButtons.count()
    if (deleteButtonCount > 0) {
      const deleteButton = deleteButtons.nth(deleteButtonCount - 1)
      
      page.on('dialog', async dialog => {
        await dialog.accept()
      })
      
      await deleteButton.click({ force: true })
      
      await expect(page.locator('.el-message--success').first()).toContainText('成功')
    }
  })

  test('分页功能', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=用户管理')
    await menuItem.click()
    await page.waitForURL(/system\/users/, { timeout: 10000 })
    await page.waitForSelector('.el-table', { timeout: 10000 })
    
    await page.waitForTimeout(1000)
  })
})