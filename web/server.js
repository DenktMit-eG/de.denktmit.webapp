const path = require('path');
const express = require('express');

const app = express();
const port = process.env.PORT || 3000; // Use port 3000 or specify your preferred port

const livereload = require("livereload");
const connectLiveReload = require("connect-livereload");
const liveReloadServer = livereload.createServer();
liveReloadServer.server.once("connection", () => {
    setTimeout(() => {
        liveReloadServer.refresh("/");
    }, 100);
});

app.use(connectLiveReload());


const resources = path.join(__dirname, 'src', 'main', 'resources')

// Serve static files from the /static/ directory
app.use('/', express.static(path.join(resources, 'static')));
app.use('/', express.static(path.join(resources, 'public')));

// Serve static files from the /templates/ directory mapped to the root path '/'
const templates = path.join(resources, 'templates');
app.use('/', express.static(templates));

// Serve HTML files directly for the root path '/'
app.get('/', (req, res) => {
    res.sendFile(path.join(templates, 'form.html'));
});

// Handle errors with a custom 404 page
app.use((req, res) => {
    res.status(404).sendFile(path.join(templates, 'error', '404.html'));
});

// Start the server
app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});
