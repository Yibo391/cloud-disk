// src/router/index.js

import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import FileList from '../views/FileList.vue'
import Upload from '../views/Upload.vue'
import Layout from '../components/Layout.vue'
import Home from '../views/Home.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { public: true },
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresAuth: false },
  },
  {
    path: '/',
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: 'files',
      },
      {
        path: 'files',
        name: 'FileList',
        component: FileList,
        meta: { requiresAuth: true },
      },
      {
        path: 'upload',
        name: 'Upload',
        component: Upload,
        meta: { requiresAuth: true },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login',
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

// Navigation Guard
router.beforeEach((to, from, next) => {
  const isLoggedIn = !!localStorage.getItem('user')

  if (to.meta.requiresAuth && !isLoggedIn) {
    // If route requires auth and user is not logged in, redirect to login
    next('/login')
  } else if (!to.meta.requiresAuth && isLoggedIn && (to.path === '/login' || to.path === '/register')) {
    // If route is public and user is logged in, redirect to home
    next('/files')
  } else {
    next()
  }
})

export default router
