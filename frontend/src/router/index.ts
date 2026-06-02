import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../components/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('../views/Home.vue')
      },
      {
        path: '/system/users',
        name: 'Users',
        component: () => import('../views/system/Users.vue')
      },
      {
        path: '/system/roles',
        name: 'Roles',
        component: () => import('../views/system/Roles.vue')
      },
      {
        path: '/system/menus',
        name: 'Menus',
        component: () => import('../views/system/Menus.vue')
      },
      {
        path: '/system/depts',
        name: 'Depts',
        component: () => import('../views/system/Depts.vue')
      },
      {
        path: '/system/announcements',
        name: 'Announcements',
        component: () => import('../views/system/Announcements.vue')
      },
      {
        path: '/system/dict-types',
        name: 'DictTypes',
        component: () => import('../views/system/DictTypes.vue')
      },
      {
        path: '/system/dict-items',
        name: 'DictItems',
        component: () => import('../views/system/DictItems.vue')
      },
      {
        path: '/settings',
        name: 'Settings',
        component: () => import('../views/Settings.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth) {
    const accessToken = userStore.accessToken
    
    if (!accessToken) {
      next('/login')
      return
    }
    
    if (userStore.isTokenExpired()) {
      userStore.logout()
      next('/login')
      return
    }
    
    next()
  } else if (to.path === '/login' && userStore.accessToken && !userStore.isTokenExpired()) {
    next('/')
  } else {
    next()
  }
})

export default router
