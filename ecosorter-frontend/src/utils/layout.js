export const DEFAULT_AVATAR = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

export const createBreadcrumbUpdater = (routeMap) => {
  return (route) => {
    const routeInfo = routeMap[route.path]
    return {
      category: routeInfo?.category || '',
      page: routeInfo?.page || ''
    }
  }
}

export const createRouteWatcher = (route, updateBreadcrumb) => {
  return () => {
    const { category, page } = updateBreadcrumb(route)
    return { category, page }
  }
}
