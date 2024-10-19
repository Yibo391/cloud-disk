import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import FileList from '../views/FileList.vue'
import Upload from '../views/Upload.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  {
    path: '/',
    component: () => import('../components/Layout.vue'),
    children: [
      { path: 'files', name: 'FileList', component: FileList },
      { path: 'upload', name: 'Upload', component: Upload },
      { path: '', redirect: 'files' },
    ],
  },
  { path: '/:pathMatch(.*)*', redirect: '/login' },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})


export default router


router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/register']
  const authRequired = !publicPages.includes(to.path)
  const isLoggedIn = !!localStorage.getItem('user')

  if (authRequired && !isLoggedIn) {
    return next('/login')
  }
  next()
})
