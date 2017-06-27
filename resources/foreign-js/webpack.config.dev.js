// const webpack = require('webpack');
const path = require("path");

const config = {
    entry: "./mappings.js",
    output: {
        path: path.resolve(__dirname, "../public", "js", "build"),
        filename: "bundle.js"
    }
};

module.exports = config;
