import '../css/webapp.css'
import typescriptLogo from './typescript.svg'
import viteLogo from './img/vite.svg'
import {setupCounter} from './counter.ts'

const app = document.querySelector<HTMLDivElement>('#app');
if (app) {
    app.innerHTML = `
        <article style="text-align: center; border: 1px dashed #ccc; border-radius: 12px; padding: 2rem; margin: 2rem 0;">
            <div style="display: flex; align-items: center; justify-content: center; gap: 0.75rem; margin-bottom: 1.5rem;">
                <a href="https://vite.dev" target="_blank">
                  <img src="${viteLogo}" class="logo" alt="Vite logo" style="height: 2em; vertical-align: middle;" />
                </a>
                <a href="https://www.typescriptlang.org/" target="_blank">
                  <img src="${typescriptLogo}" class="logo vanilla" alt="TypeScript logo" style="height: 2em; vertical-align: middle;" />
                </a>
                <h2 style="margin: 0; font-size: 1.25em;">
                    Dynamically generated with TypeScript/JavaScript
                </h2>
            </div>

            <div class="card">
              <button id="counter" type="button"></button>
            </div>
            
            <p class="read-the-docs" style="margin-top: 1rem; color: #888;">
              Click on the Vite and TypeScript logos to learn more
            </p>
        </article>`
    setupCounter(document.querySelector<HTMLButtonElement>('#counter')!)
}

