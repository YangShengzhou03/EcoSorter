module.exports = {
  transpileDependencies: true,
  lintOnSave: 'warning',
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  }
};
