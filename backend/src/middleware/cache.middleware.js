const cache = {};

// Simple In-Memory Cache middleware
// Duration in seconds
exports.simpleCache = (duration) => (req, res, next) => {
    if (req.method !== 'GET') {
        return next();
    }

    const key = req.originalUrl || req.url;
    const cachedResponse = cache[key];

    if (cachedResponse && (Date.now() - cachedResponse.time < duration * 1000)) {
        console.log(`⚡ Serving from cache: ${key}`);
        return res.json(cachedResponse.data);
    } else {
        res.sendResponse = res.json;
        res.json = (body) => {
            cache[key] = {
                time: Date.now(),
                data: body
            };
            res.sendResponse(body);
        };
        next();
    }
};
