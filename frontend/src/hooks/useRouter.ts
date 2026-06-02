import { useRouter as useVueRouter, useRoute } from 'vue-router'

export const useRouter = () => {
  const router = useVueRouter()
  const route = useRoute()

  const push = (path: string | object) => {
    return router.push(path)
  }

  const replace = (path: string | object) => {
    return router.replace(path)
  }

  const go = (n: number) => {
    router.go(n)
  }

  const back = () => {
    router.back()
  }

  const forward = () => {
    router.forward()
  }

  const getQuery = (key?: string) => {
    if (key) {
      return route.query[key]
    }
    return route.query
  }

  const getParam = (key?: string) => {
    if (key) {
      return route.params[key]
    }
    return route.params
  }

  const reload = () => {
    router.replace({ ...route, query: { ...route.query, t: Date.now() } })
  }

  return {
    router,
    route,
    push,
    replace,
    go,
    back,
    forward,
    getQuery,
    getParam,
    reload
  }
}
