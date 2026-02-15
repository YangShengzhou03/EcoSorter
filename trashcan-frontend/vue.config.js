module.exports = {
  transpileDependencies: true,
  lintOnSave: 'warning',
  devServer: {
    port: 3000,
    proxy: {
      '/api/auth': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/api/trashcan': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/api/banners': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/api/upload': {
        target: 'http://localhost:9000',
        changeOrigin: true
      },
      '/api/recognition': {
        target: 'http://localhost:9000',
        changeOrigin: true
      }
    }
  }
};
