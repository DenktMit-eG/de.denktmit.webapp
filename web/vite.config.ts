// vite.config.js

import {defineConfig} from 'vite'
import {resolve} from 'path'

const projectRoot = resolve(__dirname)
const entryDir = resolve(projectRoot, 'src', 'main', 'resources')
const staticDir = resolve(entryDir, 'static')
const tsDir = resolve(projectRoot, 'src', 'main', 'typescript')
const cssDir = resolve(projectRoot, 'src', 'main', 'css')

type RewriteRule = {
  from: string,
  to: string
}

const rewrites: RewriteRule[] = [
  {
    from: '/@basePath',
    to: '/src/main/resources/templates'
  },
  {
    from: '/@static',
    to: '/src/main/resources/static'
  },
  {
    from: '/@ts',
    to: '/src/main/typescript/'
  },
  {
    from: '/@css',
    to: '/src/main/css/'
  },
]
const allowedExtensions = [".js", ".ts", ".css", ".jpg", ".jpeg", ".png", ".gif", ".svg", ".webp"];


const userConfig = defineConfig({
  root: projectRoot,
  publicDir: staticDir,
  build: {
    outDir: resolve(projectRoot, 'dist-dev'),
    emptyOutDir: true,
  },
  optimizeDeps: {
    entries: [
      '!target/**',
      '!src/main/asciidoc/**',
      '!src/main/css/**',
      '!src/main/kotlin/**',
    ],
  },
  resolve: {
    alias: {
      '@ts': tsDir,
      '@styles': cssDir,
    }
  },
  server: {
    fs: {
      strict: true,
      allow: [
        entryDir,
        tsDir,
        cssDir,
        staticDir
      ]
    },
  },
  plugins: [
    {
      name: 'rewrite-middleware',
      configureServer(serve) {
        serve.middlewares.use((req, res, next) => {
          const rule = rewrites.find(rule => req.url && req.url.startsWith(rule.from))
          if (rule) {
            req.url = req.url!.replace(rule.from, rule.to)

            const ext = req.url.split('.').pop();
            if (!allowedExtensions.includes(`.${ext}`)) {
              // Redirect to a final URL for other types
              res.writeHead(302, {Location: `${req.url}`});
              return res.end()
            }
          }
          next()
        })
        serve.middlewares.use((req, res, next) => {
          // If serving an HTML file, set UTF-8 content header
          if (req.url?.endsWith('.html')) {
            res.setHeader('Content-Type', 'text/html; charset=utf-8')
          }
          next();
        });

      }
    }
  ]
});

export default userConfig
