module.exports = {
  transpileDependencies: true,
  lintOnSave: 'warning',
  devServer: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:9000',
        changeOrigin: true
      }
    }
  }
};
