export const DEFAULT_AVATAR = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

export function createBreadcrumbUpdater(routeMap) {
  return (route) => {
    const path = route.path
    const matched = routeMap[path]
    if (matched) {
      return {
        category: matched.category,
        page: matched.page
      }
    }
    return { category: '', page: '' }
  }
}
