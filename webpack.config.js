const path = require('path');
const DIST = 'webpack-dist'
const WriteFilePlugin = require('write-file-webpack-plugin');

module.exports = {
  entry: './static/js/index.js',
  output: {
    path: path.resolve(__dirname, DIST),
    filename: 'bundle.js'
  },
  devtool: 'source-map',
  mode: 'development',
  plugins: [
    new WriteFilePlugin()
  ],
  devServer: {
    contentBase: path.resolve(__dirname, DIST),
    port: 10000,
    proxy: {
        '/api': {
            target: 'http://localhost:8080',
            secure: false,
            pathRewrite: {"^/api" : "/my-budget/rest"}
        }
    },
    historyApiFallback: {
        index: 'index.html'
    }
  },
    module: {
        rules: [{
            test: /\.scss$/,
            use: [{
                loader: "style-loader" // creates style nodes from JS strings
            }, {
                loader: "css-loader" // translates CSS into CommonJS
            }, {
                loader: "sass-loader" // compiles Sass to CSS
            }]
        }]
    }
};
