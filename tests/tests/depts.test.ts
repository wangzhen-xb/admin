import { test, expect } from '@playwright/test'

test.describe('部门管理功能测试', () => {
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
    await page.waitForTimeout(5000)
  })

  test('进入部门管理页面', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=部门管理')
    await menuItem.click()
    await page.waitForURL(/system\/depts/, { timeout: 10000 })
    
    await expect(page).toHaveURL(/system\/depts/)
    await expect(page.locator('h2')).toHaveText('部门管理')
  })

  test('搜索部门', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=部门管理')
    await menuItem.click()
    await page.waitForURL(/system\/depts/, { timeout: 10000 })
    await page.waitForSelector('.el-table', { timeout: 10000 })
    
    const searchButton = page.locator('button:has-text("搜索")')
    await searchButton.click()
    
    await page.waitForTimeout(500)
  })

  test('添加部门', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=部门管理')
    await menuItem.click()
    await page.waitForURL(/system\/depts/, { timeout: 10000 })
    
    await page.click('button:has-text("新增")')
    await page.waitForTimeout(1000)
    
    await page.fill('input[placeholder*="部门名称"]', '测试部门')
  })

  test('编辑部门', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=部门管理')
    await menuItem.click()
    await page.waitForURL(/system\/depts/, { timeout: 10000 })
    await page.waitForSelector('.el-table', { timeout: 10000 })
    
    await page.waitForTimeout(1000)
    
    const editButtons = page.locator('.el-table .el-button--text')
    const buttonCount = await editButtons.count()
    if (buttonCount >= 2) {
      const editButton = editButtons.first()
      await editButton.click({ force: true })
      
      await page.waitForSelector('.el-dialog', { timeout: 10000 })
      
      await page.fill('input[placeholder*="部门名称"]', '更新部门')
      
      const saveButton = page.locator('.el-dialog').locator('button:has-text("保存")')
      await saveButton.click()
      
      await expect(page.locator('.el-message--success').first()).toContainText('成功')
    }
  })

  test('删除部门', async ({ page }) => {
    const menuItem = page.locator('aside').locator('text=部门管理')
    await menuItem.click()
    await page.waitForURL(/system\/depts/, { timeout: 10000 })
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
})