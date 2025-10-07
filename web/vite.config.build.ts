// vite.config.js

import {defineConfig} from 'vite'
import dts from 'vite-plugin-dts'
import {resolve} from 'path'

const projectRoot = resolve(__dirname)          // equals …/web/nodejs

const userConfig = defineConfig({
  css: {
    devSourcemap: true,
  },
  build: {
    minify: 'esbuild',
    outDir: resolve(projectRoot, 'dist'),
    emptyOutDir: true,
    lib: {
      entry: resolve(projectRoot, 'src', 'main', 'typescript', 'lib.ts'),
      name: 'webapp-lib',
      formats: ['es', 'umd', 'cjs'],
      fileName: 'webapp-lib'
    },
    sourcemap: false,
  },
  plugins: [
    dts({
      rollupTypes: true,
    })
  ]
});

export default userConfig
